package com.kir.mycomposesample.labratory.hiltlib

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject
import jakarta.inject.Qualifier
import jakarta.inject.Singleton

/****************************************** Binds 시작 ***********************************************************************/
interface Logger {
    fun log(message: String)
}

// 인터페이스를 구현하는 클래스입니다.
class AppLogger @Inject constructor() : Logger {
    override fun log(message: String) {
        println("AppLogger: $message")
    }
}

@Module // Module을 정의하여 Hilt에게 'Logger' 인터페이스에 대한 구현체를 알려줍니다.
@InstallIn(SingletonComponent::class) // 이 모듈이 ApplicationComponent(앱 전체)에 설치됨을 지정합니다.
abstract class LoggerModule {

    // @Binds: 인터페이스(Logger)를 구현체(AppLogger)에 연결합니다.
    // 함수 파라미터: 인터페이스를 구현한 인스턴스(제공자)
    // return: hilt에 주입하고자하는 녀석.즉 interface(직접 구현불가능하기때문에 Module를 사용).
    @Binds
    @Singleton // @Singleton: 이 인스턴스를 앱 전체에서 단 하나의 인스턴스만 사용하도록 지정합니다.
    abstract fun bindLogger(appLogger: AppLogger): Logger
}

/****************************************** Binds 끝  ***********************************************************************/
/*******************************************************************
 * Binds 와 Providers는 같은 주입을 사용하지만 상황에 따라 사용되는게 다르다.    *
 * 제일 다른점은 @Binds는 @Inject 생성자를 가질 떄 사용할 수 있고             *
 * @Provides는 @Inject없을 떄 @Providers에서 직접 생성자를 사용 할 수 있다. *
 *******************************************************************/
/****************************************** Provides 시작 ********************************************************************/
/**
 * 만약 MyHome이 라이브러리에 있는 class라면 이걸 주입하기위해서는 @Provides를 사용 할 수 있다.
 * @Inject를 사용하여 Hilt에서 주입된 MyHome를 꺼내면 자동으로 @Provides로 주입한 provideSetupMyHome()의 내용을 꺼낼 수 있다.
 * (@Inject lateinit var myHome: MyHome 로 사용)
 */
class MyHome {
    private var address: String = ""
    fun setAddress(address: String): MyHome {
        this.address = address
        return this
    }

    fun getAddress(): String {
        return address
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ProvideMyHome {
    @Provides
    @Singleton
    fun provideSetupMyHome(): MyHome {
        return MyHome()
            .setAddress("경기도 부천시")
    }

    /**
     * 하지만 동일한 MyHome의 유형을 서로 다르게 꺼내고 싶을 떄도 있다.
     * 이럴 떄 @Qualifier, @Retention를 사용한다
     */
    @MyHomeSoSa
    @Provides
    fun provideSetupMyHomeSoSa(): MyHome {
        return MyHome()
            .setAddress("나의 소사집은 소사본동")
    }
    @MyHomeSinLim
    @Provides
    fun provideSetupMyHomeSinLim(): MyHome {
        return MyHome()
            .setAddress("나의 신림집은 은천동")
    }

    //이런 식으로 파라미터에도 쓸 수 있고
    //에러 위해 주석
//    @Provides
//    fun setUpHome(@MyHomeSoSa sosaHome: MyHome): MyHome {
//        return MyHome().setAddress("추가된 주소: ${sosaHome.getAddress()}")
//    }

    //클래스의 Inject등록시에도 쓸 수 있다
//    class MyHomeImpl @Inject constructor(@MyHomeSinLim val sinLimHome: MyHome) {
//        fun getAddr(): String {
//            return sinLimHome.getAddress()
//        }
//    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyHomeSinLim
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MyHomeSoSa
/****************************************** Provides 끝 *********************************************************************/