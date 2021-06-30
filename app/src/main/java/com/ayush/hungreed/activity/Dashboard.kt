package com.ayush.hungreed.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.ayush.hungreed.R
import com.ayush.hungreed.fragment.*
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    lateinit var coordinatorLayout: CoordinatorLayout
    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        drawerLayout=findViewById(R.id.drawerlayout)

        frameLayout=findViewById(R.id.framelayout)
        navigationView=findViewById(R.id.navigationview)
        coordinatorLayout=findViewById(R.id.coordinatorlayout)

        setuptoolbar()
        openHome()

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@Dashboard,drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        navigationView.setNavigationItemSelectedListener{
            if(it.itemId== R.id.logout){
                it.isCheckable=false
            }
            else{
                if(previousMenuItem!=null){
                    previousMenuItem?.isChecked=false
                }
                it.isCheckable=true
                it.isChecked=true
                previousMenuItem=it
            }

            when(it.itemId){
                R.id.home ->{
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.profile ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,
                        ProfileFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="My Profile"
                }
                R.id.favorite ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,
                        FavoriteFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Favorites"
                }
                R.id.history ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,
                        HistoryFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="Order History"
                }
                R.id.faq ->{
                    supportFragmentManager.beginTransaction().replace(R.id.framelayout,
                        FaqFragment()).commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title="FAQs"
                }
                R.id.logout ->{
                    val dialog=AlertDialog.Builder(this)
                    dialog.setMessage("Are you sure want to logout")
                    dialog.setPositiveButton("Yes"){text,listener->
                        finishAffinity()
                        startActivity(Intent(this, user::class.java))
                    }
                    dialog.setNegativeButton("No"){text,listener->
                    }
                    dialog.create().show()
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setuptoolbar(){
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title="Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun openHome(){
        supportFragmentManager.beginTransaction().replace(R.id.framelayout, HomeFragment()).commit()
        supportActionBar?.title="Home"
        navigationView.setCheckedItem(R.id.home)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        if(id==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag=supportFragmentManager.findFragmentById(R.id.framelayout)
        when(frag){
            !is HomeFragment -> openHome()
            else->super.onBackPressed()
        }
    }
}