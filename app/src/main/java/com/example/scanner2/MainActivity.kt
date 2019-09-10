package com.example.scanner2

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.appscanner.Api
import com.example.appscanner.DiscoResponse


import com.example.appscanner.RetrofitClient
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        txtGenero.focusable
        txtBanda.focusable
        txtano.focusable
        txtNombre.focusable



    }



    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_guardar -> {
                    val disco = Disco(
                        txtNombre.text.toString(),
                        txtBanda.text.toString(),
                        txtano.text.toString(),
                        txtGenero.text.toString()
                    )
                    var gson = Gson()
                    var jsonString = gson.toJson(disco)
                    RetrofitClient.instance.NuevoDisco(disco)
                        .enqueue(object : Callback<DiscoResponse> {
                            override fun onFailure(call: Call<DiscoResponse>, t: Throwable) {
                                Snackbar.make(findViewById(R.id.main_layout_id), t.message.toString(), Snackbar.LENGTH_LONG).show();
                            }
                            override fun onResponse(
                                call: Call<DiscoResponse>,
                                response: Response<DiscoResponse>
                            ) {
                                Snackbar.make(findViewById(R.id.main_layout_id), response.body()?.Mensaje.toString(), Snackbar.LENGTH_LONG).show();
                            }
                        })
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_scanner -> {
                    val scanner = IntentIntegrator(this)
                    scanner.initiateScan()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()

                } else {
                    if (result.contents == null) {
                    } else {
                        txtNombre.setText(result.contents.toString())
                        RetrofitClient.instance.disco(result.contents)
                            .enqueue(object : Callback<DiscoResponse> {
                                override fun onFailure(call: Call<DiscoResponse>, t: Throwable) {
                                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG)
                                        .show()
                                }
                                override fun onResponse(
                                    call: Call<DiscoResponse>,
                                    response: Response<DiscoResponse>
                                ) {
                                    Snackbar.make(findViewById(R.id.main_layout_id), response.message(), Snackbar.LENGTH_LONG).show();
                                    txtNombre.setText(response.body()?.Nombre.toString())
                                    txtBanda.setText(response.body()?.Banda.toString())
                                    txtano.setText(response.body()?.Ano.toString())
                                    txtGenero.setText(response.body()?.Genero.toString())
                                }
                            })
                    }
                    // Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG)
                    //     .show()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}
