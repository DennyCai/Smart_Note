package com.denny.android.idea_note

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarActivity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.denny.android.idea_note.utils.FileUtil

import java.io.File


public class PhotoActivity : AppCompatActivity() {

    private var mPager: ViewPager? = null
    private val mPath: TextView? = null
    private var mId: String? = null
    private var photos: Array<File>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        mId = getIntent().getExtras().getString(NOTE_ID)
        photos = FileUtil.listFilesStart(FileUtil.getImagesPath().getAbsolutePath(), mId)
        initView()

    }

    private fun initView() {
        mPager = findViewById(R.id.pager) as ViewPager

        mPager!!.setAdapter(object : FragmentStatePagerAdapter(getSupportFragmentManager()) {
            override fun getItem(position: Int): Fragment {
                return PhotoFragment.newInstance(photos!![position])
            }

            override fun getCount(): Int {
                return photos!!.size()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item!!.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    public class PhotoFragment private constructor(private val photo: File) : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater!!.inflate(R.layout.fragment_photo, container, false)
        }

        override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            Log.e("Photo", photo.getAbsolutePath())

            val imageView = view!!.findViewById(R.id.images) as ImageView
            val mPath = view.findViewById(R.id.path) as TextView
            mPath.setText(photo.getName())
            val ops = BitmapFactory.Options()
            ops.inSampleSize = 1
            ops.inJustDecodeBounds = false

            val bitmap = BitmapFactory.decodeFile(photo.getAbsolutePath(), ops)
            imageView.setImageBitmap(bitmap)
        }

        companion object {


            public fun newInstance(f: File): Fragment {
                return PhotoFragment(f)
            }
        }
    }

    companion object {

        public val PHOTOS_PATH: String = "PHOTOS_PATH"
        public val NOTE_ID: String = "note_id"
    }
}
