package com.wangari.agritech.ui.screens.login
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.input.ImeAction
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.unit.dp
//import com.wangari.agritech.R
//import com.wangari.agritech.data.AuthViewModel
//
//@Composable
//fun login(
//    onLoginSuccess: () -> Unit,
//    onNavigateToSignup: () -> Unit,
//    viewModel: AuthViewModel // Keep as viewModel to match AppNavHost for now
//) {
//    val isLoading by viewModel.isLoading.collectAsState()
//    val error by viewModel.error.collectAsState()
//    val currentUser by viewModel.currentUser.collectAsState()
//
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    // Effect to handle successful login and navigate
//    LaunchedEffect(currentUser) {
//        if (currentUser != null) {
//            onLoginSuccess()
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            // Logo
//            Image(
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = "FarmConnect Logo",
//                modifier = Modifier
//                    .height(100.dp)
//                    .fillMaxWidth(),
//                contentScale = ContentScale.Fit
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            // Login form
//            Card(
//                modifier = Modifier.fillMaxWidth(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Welcome Back",
//                        style = MaterialTheme.typography.headlineMedium,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Email field
//                    OutlinedTextField(
//                        value = email,
//                        onValueChange = { email = it },
//                        label = { Text("Email") },
//                        modifier = Modifier.fillMaxWidth(),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Email,
//                            imeAction = ImeAction.Next
//                        )
//                    )
//
//                    Spacer(modifier = Modifier.height(8.dp))
//
//                    // Password field
//                    OutlinedTextField(
//                        value = password,
//                        onValueChange = { password = it },
//                        label = { Text("Password") },
//                        modifier = Modifier.fillMaxWidth(),
//                        visualTransformation = PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(
//                            keyboardType = KeyboardType.Password,
//                            imeAction = ImeAction.Done
//                        )
//                    )
//
//                    // Error message
//                    if (error != null) {
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text(
//                            text = error ?: "",
//                            color = MaterialTheme.colorScheme.error,
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Login button
//                    Button(
//                        onClick = {
//                            viewModel.signIn( email,password,navController)
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        enabled = !isLoading && email.isNotEmpty() && password.isNotEmpty()
//                    ) {
//                        if (isLoading) {
//                            CircularProgressIndicator(
//                                color = MaterialTheme.colorScheme.onPrimary,
//                                modifier = Modifier.height(24.dp)
//                            )
//                        } else {
//                            Text("Log In")
//                        }
//                    }
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    // Sign up link
//                    TextButton(onClick = onNavigateToSignup) {
//                        Text("Don't have an account? Sign Up")
//                    }
//                }
//            }
//        }
//    }
//}

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wangari.agritech.R
import com.wangari.agritech.data.AuthViewModel
import com.wangari.agritech.navigate.AppDestinations.ROUTE_REGISTER

@Composable
fun LoginScreen(navController: NavController){
    val authViewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf(value = "") }
    var password by remember { mutableStateOf(value = "") }
    val context = LocalContext.current
    Column (modifier = Modifier.fillMaxHeight().fillMaxWidth(), verticalArrangement = Arrangement.Center){
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .wrapContentWidth().align(Alignment.CenterHorizontally)

        )

        Image(painter = painterResource(R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .height(180.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(value = email, onValueChange = {newEmail->email=newEmail}, label = { Text(text = "Enter Email") }, placeholder = { Text(text = "Please enter your email") }, modifier = Modifier.wrapContentWidth().align(
            Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = password,
            onValueChange = {newPassword -> password = newPassword},
            label = { Text(text = "Enter Password") },
            placeholder = { Text(text = "Please enter password") },
            modifier = Modifier.wrapContentWidth().align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            authViewModel.signIn(navController, email, password)
        }, modifier = Modifier.wrapContentWidth().align(Alignment.CenterHorizontally), colors = ButtonDefaults.buttonColors(
            Color.Black)) {
            Text(text = "Login")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = buildAnnotatedString { append("If not registered, Register here ") }, modifier = Modifier.wrapContentWidth().align(
            Alignment.CenterHorizontally).clickable{
            navController.navigate(ROUTE_REGISTER)
        } )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen(rememberNavController())
}