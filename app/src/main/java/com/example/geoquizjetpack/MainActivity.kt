package com.example.geoquizjetpack

import android.content.res.Configuration
import android.graphics.Paint
import android.os.Bundle
import android.view.Surface
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyScreen()
        }
    }
}

@Composable
fun MyScreen(){
    HandleOrientationChanges()
}

@Composable
fun HandleOrientationChanges(){
    val configuration= LocalConfiguration.current
    val isLandscape=configuration.orientation== Configuration.ORIENTATION_LANDSCAPE
    QuizLayout(isLandscape)
}

@Composable
fun QuizLayout(isHorizontal: Boolean) {
    val context= LocalContext.current

    val mQuestionBank=listOf(Question(R.string.question_australia,true),
        Question(R.string.question_asia,true),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_oceans,true)
    )
    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    val currentQuestion=mQuestionBank[currentIndex]

    val onClickedAns: (Boolean) -> Unit = { userAnswer ->
        val isCorrect = userAnswer == currentQuestion.answer
        val msg = if (isCorrect) context.getString(R.string.correct_toast) else context.getString(R.string.incorrect_toast)
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
    val onClickedNext: () -> Unit = {
        currentIndex = (currentIndex + 1) % mQuestionBank.size
    }
    if(isHorizontal){
        LandscapeLayout(currentQuestion,onClickedAns,onClickedNext)
    } else {
        PortraitLayout(currentQuestion,onClickedAns,onClickedNext)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitLayout(
    currentQuestion: Question,
    onAnswerClicked: (Boolean) -> Unit,
    onNextClicked: () -> Unit
) {

    Scaffold(
        topBar = { TopAppBar(title = { Text("GeoQuiz") }) }
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Centro
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(currentQuestion.textResId),
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(onClick = { onAnswerClicked(true)} ) {
                        Text(stringResource(R.string.btnTrue))
                    }
                    FilledTonalButton(onClick = { onAnswerClicked(false)} ) {
                        Text(stringResource(R.string.btnFalse))
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    FilledTonalButton(onClick = { onNextClicked() }) {
                        Text(text=stringResource(R.string.btnNext))
                    }

                }
            }


        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeLayout(
    currentQuestion: Question,
    onAnswerClicked: (Boolean) -> Unit,
    onNextClicked: () -> Unit
){
    Scaffold(
        topBar = {
            // Barra superior con el título
            TopAppBar(
                title = { Text("GeoQuiz") })
        },
        content = { innerPadding ->
            // Caja que contiene el contenido principal
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .align ( Alignment.Center )
                          .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Pregunta
                    Text(
                        text = "Canberra is the capital of Australia.",
                        textAlign = TextAlign.Center

                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botones de respuesta
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FilledTonalButton(
                            onClick = { /* Acción para respuesta True */ },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(text = "TRUE")
                        }
                        FilledTonalButton(
                            onClick = { /* Acción para respuesta False */ },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(text = "FALSE")
                        }
                    }

                }

                // Botón "Next"
                FilledTonalButton(onClick = { /* Acción para siguiente pregunta */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(text = "NEXT")
                }
            }
        }
    )


}



