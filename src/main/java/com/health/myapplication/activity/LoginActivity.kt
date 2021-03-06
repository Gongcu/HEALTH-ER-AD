package com.health.myapplication.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.health.myapplication.R
import com.health.myapplication.application.GlobalApplication
import com.health.myapplication.model.CommunityUser
import com.health.myapplication.repository.Repository
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    private lateinit var callback: SessionCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val intent = Intent(this, LoadingActivity::class.java)
        startActivity(intent)

        callback = SessionCallback()
        Session.getCurrentSession().addCallback(callback)
        Session.getCurrentSession().checkAndImplicitOpen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(callback)
    }

    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            // 로그인 세션이 열렸을 때
            UserManagement.getInstance().me( object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    // 로그인이 성공했을 때
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    (application as GlobalApplication).userId = result!!.id
                    Log.d("kakaoID = ", result!!.id.toInt().toString())
                    Repository(application).postUser(CommunityUser(result!!.id.toInt()))
                    startActivity(intent)
                    finish()
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    // 로그인 도중 세션이 비정상적인 이유로 닫혔을 때
                    Toast.makeText(
                            this@LoginActivity,
                            "세션이 닫혔습니다. 다시 시도해주세요 : ${errorResult.toString()}",
                            Toast.LENGTH_SHORT).show()
                }
            })
        }
        override fun onSessionOpenFailed(exception: KakaoException?) {
            // 로그인 세션이 정상적으로 열리지 않았을 때
            if (exception != null) {
                com.kakao.util.helper.log.Logger.e(exception)
                Toast.makeText(
                        this@LoginActivity,
                        "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요 : $exception",
                        Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun redirectSignupActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}