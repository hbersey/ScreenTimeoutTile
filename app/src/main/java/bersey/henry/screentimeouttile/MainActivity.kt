package bersey.henry.screentimeouttile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timeoutRecycler: RecyclerView = findViewById(R.id.timeoutRecycler);
        timeoutRecycler.layoutManager = LinearLayoutManager(this);
        timeoutRecycler.adapter = TimeoutRecyclerAdapter(this);
    }
}