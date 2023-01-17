package com.example.liveasy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class VerifyActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var verifycontinue: Button
    private lateinit var requestagain: TextView
    private lateinit var inputotp1: EditText
    private lateinit var inputotp2: EditText
    private lateinit var inputotp3: EditText
    private lateinit var inputotp4: EditText
    private lateinit var inputotp5: EditText
    private lateinit var inputotp6: EditText

    private  lateinit var OTP: String
    private  lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mobilenumber: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)


        OTP= intent.getStringExtra("OTP").toString()
        resendToken = intent.getParcelableExtra("resendToken")!!
        mobilenumber= intent.getStringExtra("mobilenumber")!!

        init()
        addTextChangedListener()

        requestagain.setOnClickListener {
            requestVerificationCode()
        }
        verifycontinue.setOnClickListener {
            //collect otp from all the edit texts
            val typedOTP= (inputotp1.text.toString()+inputotp2.text.toString()+inputotp3.text.toString()+inputotp4.text.toString()+inputotp5.text.toString()+inputotp6.text.toString())

            if(typedOTP.isNotEmpty()){
                if(typedOTP.length==6){
                    val credential: PhoneAuthCredential= PhoneAuthProvider.getCredential(
                        OTP, typedOTP
                    )
                    signInWithPhoneAuthCredential(credential)
                }else{
                    Toast.makeText(this,"Please enter correct OTP", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"Please enter OTP", Toast.LENGTH_SHORT).show()
            }
        }
        findViewById<TextView>(R.id.tv_number1).text=mobilenumber
    }

    private  fun  requestVerificationCode(){
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(mobilenumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendToken)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                Log.d("TAG","onVerificationFailed : ${e.toString()}" )
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Log.d("TAG","onVerificationFailed : ${e.toString()}" )
            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            // Save verification ID and resending token so we can use them later
            OTP= verificationId
            resendToken= token
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this,"Authenticate Successfully",Toast.LENGTH_SHORT).show()
                    sendToSelectProfile()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d("TAG","signInWithPhoneAuthCredential: ${task.exception.toString()}")
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    private  fun sendToSelectProfile(){
        startActivity(Intent(this, SelectYourProfile::class.java))
    }

    private fun addTextChangedListener(){
        inputotp1.addTextChangedListener (EditTextWatcher((inputotp1)))
        inputotp2.addTextChangedListener (EditTextWatcher((inputotp2)))
        inputotp3.addTextChangedListener (EditTextWatcher((inputotp3)))
        inputotp4.addTextChangedListener (EditTextWatcher((inputotp4)))
        inputotp5.addTextChangedListener (EditTextWatcher((inputotp5)))
        inputotp6.addTextChangedListener (EditTextWatcher((inputotp6)))
    }

    private  fun init(){
        auth= FirebaseAuth.getInstance()
        verifycontinue=findViewById(R.id.verify_continue)
        requestagain = findViewById(R.id.tv_request_Again)
        inputotp1= findViewById(R.id.otpEditText1)
        inputotp2= findViewById(R.id.otpEditText2)
        inputotp3= findViewById(R.id.otpEditText3)
        inputotp4= findViewById(R.id.otpEditText4)
        inputotp5= findViewById(R.id.otpEditText5)
        inputotp6= findViewById(R.id.otpEditText6)
    }

    inner class EditTextWatcher( private  val view: View): TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(p0: Editable?) {

            val text = p0.toString()
            when(view.id){
                R.id.otpEditText1 -> if(text.length==1)inputotp2.requestFocus()
                R.id.otpEditText2 -> if(text.length==1)inputotp3.requestFocus() else if(text.isEmpty()) inputotp1.requestFocus()
                R.id.otpEditText3 -> if(text.length==1)inputotp4.requestFocus()else if(text.isEmpty()) inputotp2.requestFocus()
                R.id.otpEditText4 -> if(text.length==1)inputotp5.requestFocus()else if(text.isEmpty()) inputotp3.requestFocus()
                R.id.otpEditText5 -> if(text.length==1)inputotp6.requestFocus()else if(text.isEmpty()) inputotp4.requestFocus()
                R.id.otpEditText6->if(text.isEmpty()) inputotp5.requestFocus()
            }
        }

    }
}