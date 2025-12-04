package com.kir.mycomposesample.labratory.hiltlib

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kir.mycomposesample.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/***
 * Hilt 라이브러리 사용(DI형식:의존성 주입)
 * 문서: https://developer.android.com/training/dependency-injection/hilt-android?hl=ko#setup
 *
 */

@AndroidEntryPoint // Hilt가 이 Activity에 대한 DI 컨테이너를 생성하고 필드 주입을 활성화합니다.
class HiltActivity : ComponentActivity() {

    // viewModels()를 사용하여 Hilt가 주입한 ViewModel 인스턴스를 가져옵니다.
    // Hilt는 HiltViewModel 의 생성자 요구 사항을 충족시켜줍니다.
    private val viewModel: HiltViewModel by viewModels()

    //@Inject를 사용하여 Hilt에 주입된 UserRepository을 가져온다.
    //보통은 개발자고 new를 해야하지만 Hilt를 사용함으로 Hilt가 관리해주기때문에 직접 사용하면 된다.(여기서 @Inject는 그런 기능이다)
    @Inject lateinit var userRepository: UserRepository

    @Inject lateinit var appLogger: AppLogger
    @Inject lateinit var myHome: MyHome
    @Inject @MyHomeSoSa lateinit var myHomeSoSa: MyHome
    @Inject @MyHomeSinLim lateinit var myHomeSinLim: MyHome


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hilt)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //# case 1
        val userName = viewModel.loadUserName()
        findViewById<TextView>(R.id.text).text = "이름은 : $userName \n @Inject var로 사용: ${userRepository.getUserName()}"

        //# case 2
        viewModel.log("로그에 찍힌 행복한 하마")
        appLogger.log("AppLogger 사용하여 로그에 찍힌 행복한 하마")

        //# case 3
        findViewById<TextView>(R.id.text_1).text = "나의 집 주소는: ${myHome.getAddress()}\n${myHomeSoSa.getAddress()}\n${myHomeSinLim.getAddress()}"
    }
}