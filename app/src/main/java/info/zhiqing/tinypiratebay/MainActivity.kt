package info.zhiqing.tinypiratebay

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import info.zhiqing.tinypiratebay.adapters.CategoryAdapter
import android.widget.Toast
import info.zhiqing.tinypiratebay.activities.AboutActivity
import info.zhiqing.tinypiratebay.functions.startSearch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val listView = findViewById(R.id.categoryListView) as RecyclerView
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = CategoryAdapter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as? SearchView
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                startSearch(this@MainActivity, query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        when(id) {
            R.id.action_faq -> {
                showFaq()
            }
            R.id.action_feedback -> {
                showFeedback()
            }
            R.id.action_about -> {
                showAbout()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showFaq() {
        AlertDialog.Builder(this)
                .setTitle(R.string.faq_dialog_title)
                .setMessage(R.string.faq_dialog_text)
                .setCancelable(true)
                .setPositiveButton(R.string.faq_dialog_button,
                        DialogInterface.OnClickListener(){ dialog, which ->
                    dialog.cancel()
                })
                .show()
    }

    private fun showFeedback() {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(resources.getString(R.string.feedback_email)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_title))
        if(intent.resolveActivity(packageManager) == null) {
            Snackbar.make(findViewById(R.id.categoryListView),
                    getString(R.string.feedback_no_email_app),
                    Snackbar.LENGTH_LONG).show()
            return
        }
        startActivity(intent)
    }

    private fun showAbout() {
        startActivity(Intent(this, AboutActivity::class.java))
    }
}
