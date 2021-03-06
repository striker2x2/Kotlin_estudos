package com.example.felipefrazao.dotdocumentaes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.example.felipefrazao.dotdocumentaes.domain.Doc
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnRenderListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.android.synthetic.main.activity_pdf.*


class PdfActivity:AppCompatActivity(), OnPageChangeListener,OnRenderListener {

    var doc: Doc? = null
    var toolbar:Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        doc = intent.getParcelableExtra(Doc.DOC_KEY)


        //val file  = File(Environment.getExternalStorageDirectory(),doc?.path)
        //fromFile(file)

        pdfView.fromAsset(doc?.path)
                .defaultPage(doc?.getActualPage(this) ?: 0)
                .scrollHandle(DefaultScrollHandle(this))
                .enableSwipe(true)
                .swipeHorizontal(true)
                .password(null)
                .enableDoubletap(true)
                .enableAnnotationRendering(true)
                .enableAntialiasing(true)
                .onPageChange(this)
                .onRender(this)
                .load()

        pdfView.minZoom = 1F
        pdfView.midZoom = 1.75F
        pdfView.maxZoom = 3F
    }

    override fun onResume() {
        super.onResume()
        toolbar?.title = doc?.language

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val id = item?.itemId

        if(id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        doc?.saveActualPage(this,page)

    }

    override fun onInitiallyRendered(nbPages: Int, pageWidth: Float, pageHeight: Float) {
        pdfView.fitToWidth(doc?.getActualPage(this) ?: 0)
    }
}