package com.fmktechnologies.weatherapp
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import im.delight.android.location.SimpleLocation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tek_satir.*
import org.json.JSONObject
import java.text.DateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var tvSehir : TextView? = null
    var location : SimpleLocation? = null
    var  latitude : String? = null
    var longitude: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



       // oankiSehriGetir(location?.getLatitude(),location?.getLongitude())
        //Toast.makeText(this,"Enlem : $latitude Boylam $longitude  Location ${location?.getLatitude() }",Toast.LENGTH_LONG).show()
        var sehirler =resources.getStringArray(R.array.sehirler)



       var adapter = ArrayAdapter.createFromResource(this,R.array.sehirler,R.layout.tek_satir)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.setAdapter(adapter)
        spinner.setOnItemSelectedListener(this)

        spinner.setSelection(1)
        verileriGetir("New York")










    }

    private fun oankiSehriGetir(lat: Double?,long : Double?){


        var calendar : Calendar = Calendar.getInstance()
        var currentDate = DateFormat.getDateInstance().format(calendar.time)

        var  days =  arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" )


        var  day = days[calendar.get(Calendar.DAY_OF_WEEK)-1]
        var sehiradi : String? = null


        val url = "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$long&appid=6d53582e594ebee4a820cff1b8b33989&units=metric"
        val havadurumuObjeRequest2 = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener<JSONObject> { response ->



                var weather =  response?.getJSONArray("weather")
                var main  =  response?.getJSONObject("main")

                var ruzgarhizi = response.getJSONObject("wind").getString("speed")
                var sicaklik = main?.getString("temp")?.toDouble()?.formatla(2)

                var nem  = main?.getString("humidity")

               sehiradi  = response?.getString("name")

                tvSehir?.setText(sehiradi)
                var resim = weather?.getJSONObject(0)?.getString("icon")
                var aciklama = weather?.getJSONObject(0)?.getString("description")
                var tarih  = currentDate


                if (resim?.contains("n")!!){


                    tvSehir?.setTextColor(Color.WHITE)
                    cons.setBackgroundResource(R.drawable.bgdark)
                    tvsehir.setTextColor(Color.WHITE)
                    tvAciklama.setTextColor(Color.WHITE)
                    tvDerece.setTextColor(Color.WHITE)
                    tvNem.setTextColor(Color.WHITE)
                    tvRuzgar.setTextColor(Color.WHITE)
                    tvTarih.setTextColor(Color.WHITE)

                }else if (resim.contains("d")){

                    tvSehir?.setTextColor(Color.BLACK)
                    cons.setBackgroundResource(R.drawable.megatron)
                    tvsehir.setTextColor(Color.BLACK)
                    tvAciklama.setTextColor(Color.BLACK)
                    tvDerece.setTextColor(Color.BLACK)
                    tvNem.setTextColor(Color.BLACK)
                    tvRuzgar.setTextColor(Color.BLACK)
                    tvTarih.setTextColor(Color.BLACK)

                }



                tvTarih.text = tarih+ " " +day+ " "
                tvAciklama.text = aciklama
                tvsehir.text = sehiradi?.toUpperCase()
                tvDerece.text = "$sicaklik°"
                tvRuzgar.text = "Wind "+ ruzgarhizi+"km/s"
                tvNem.text = "Humidity %" + nem

                var resimDosyaAdi = resources.getIdentifier("icon_" + resim.sonKarakteriSil(),"drawable",packageName)
                imgHavaDurumu.setImageResource(resimDosyaAdi)

                spinner.setTitle("Select city")


                //Toast.makeText(this,"$resim",Toast.LENGTH_LONG).show()


            }
            , Response.ErrorListener {


            }


        )




        MySingleton.getInstance(this)?.addToRequestQueue(havadurumuObjeRequest2)



    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 99){
            if (grantResults.size >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                location  = SimpleLocation(this)
                oankiSehriGetir(location?.latitude,location?.longitude)

            }else{
                spinner.setSelection(1)
            }
        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)



    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    //    Toast.makeText(this,"Position : $position,Id : $id",Toast.LENGTH_LONG).show()
        var sehirler =resources.getStringArray(R.array.sehirler)
        tvSehir = view as TextView

        if (position == 0){

            location  = SimpleLocation(this)

          /*  if (!location!!.hasLocationEnabled()){
                spinner.setSelection(1)
                Toast.makeText(this,"Open the Gps NIGGA",Toast.LENGTH_LONG).show()

                SimpleLocation.openSettings(this)
            }else{*/

                if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),99)
                }else{
                    location  = SimpleLocation(this)
                    oankiSehriGetir(location?.latitude,location?.longitude)

                }

           // }








        }

        else{

            verileriGetir(sehirler[position])


        }

        // oankiSehriGetir("30","29")

    }
    fun verileriGetir(sehir : String){

        var calendar : Calendar = Calendar.getInstance()
        var currentDate = DateFormat.getDateInstance().format(calendar.time)

        var  days =  arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" )


        var  day = days[calendar.get(Calendar.DAY_OF_WEEK)-1]

        val url = "https://api.openweathermap.org/data/2.5/weather?q=" + sehir +","+sehir+" &appid=6d53582e594ebee4a820cff1b8b33989&units=metric"
        val havadurumuObjeRequest = JsonObjectRequest(Request.Method.GET,url,null,
            Response.Listener<JSONObject> { response ->

                var weather =  response?.getJSONArray("weather")
                var main  =  response?.getJSONObject("main")

                var ruzgarhizi = response.getJSONObject("wind").getString("speed")
                var sicaklik = main?.getString("temp")?.toDouble()?.formatla(2)

                var nem  = main?.getString("humidity")
                var sehiradi = response?.getString("name")
                var resim = weather?.getJSONObject(0)?.getString("icon")
                var aciklama = weather?.getJSONObject(0)?.getString("description")
                var tarih  = currentDate


                if (resim?.contains("n")!!){


                    tvSehir?.setTextColor(Color.WHITE)
                    cons.setBackgroundResource(R.drawable.bgdark)
                    tvsehir.setTextColor(Color.WHITE)
                    tvAciklama.setTextColor(Color.WHITE)
                    tvDerece.setTextColor(Color.WHITE)
                    tvNem.setTextColor(Color.WHITE)
                    tvRuzgar.setTextColor(Color.WHITE)
                    tvTarih.setTextColor(Color.WHITE)

                }else if (resim.contains("d")){

                    tvSehir?.setTextColor(Color.BLACK)
                    cons.setBackgroundResource(R.drawable.megatron)
                    tvsehir.setTextColor(Color.BLACK)
                    tvAciklama.setTextColor(Color.BLACK)
                    tvDerece.setTextColor(Color.BLACK)
                    tvNem.setTextColor(Color.BLACK)
                    tvRuzgar.setTextColor(Color.BLACK)
                    tvTarih.setTextColor(Color.BLACK)

                }



                tvTarih.text = tarih+ " " +day+ " "
                tvAciklama.text = aciklama
                tvsehir.text = sehiradi?.toUpperCase()
                tvDerece.text = "$sicaklik°"
                tvRuzgar.text = "Wind "+ ruzgarhizi+"km/s"
                tvNem.text = "Humidity %" + nem

                var resimDosyaAdi = resources.getIdentifier("icon_" + resim.sonKarakteriSil(),"drawable",packageName)
                imgHavaDurumu.setImageResource(resimDosyaAdi)

                spinner.setTitle("Select city")


               // Toast.makeText(this,"$resim",Toast.LENGTH_LONG).show()


            }
            , Response.ErrorListener {


            }


        )



        MySingleton.getInstance(this)?.addToRequestQueue(havadurumuObjeRequest)

    }
    private fun String?.sonKarakteriSil(): String? {



        return this?.substring(0,this.length-1)

    }
    fun Double.formatla(kactanerakam : Int) = java.lang.String.format("%.0f",this)



}




