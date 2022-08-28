package bersey.henry.screentimeouttile

fun getTimeouts(): IntArray {
//    TOOD: load timeouts from shared preferences
    return intArrayOf(15, 30, 60, 300, 600, 900);
}