package com.example.btl1.dal

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.btl1.Model.Chapter
import com.example.btl1.Model.ChapterDaDoc
import com.example.btl1.Model.ChapterModel
import com.example.btl1.Model.Comment
import com.example.btl1.Model.Truyen
import com.example.btl1.Model.TruyenDaDoc
import com.example.btl1.Model.TruyenModel
import com.example.btl1.Model.TruyenYeuThich
import com.example.btl1.Model.User

class SQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "TruyenDB"
        private const val DATABASE_VERSION = 1
        const val CREATE_TABLE_CATEGORY = "CREATE TABLE category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name_category TEXT)"
        const val CREATE_TABLE_TRUYEN = "CREATE TABLE IF NOT EXISTS truyen (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tentruyen TEXT," +
                "linkhinh TEXT," +
                "linktruyen TEXT)"
        private const val CREATE_TABLE_CHAPTER = "CREATE TABLE IF NOT EXISTS chapter (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "truyenId INTEGER," +
                " tenchap TEXT," +
                "linkchap TEXT," +
                "timechap TEXT)"
        const val CREATE_TABLE_USER = "CREATE TABLE user (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_name TEXT," +
                "user TEXT," +
                "password TEXT," +
                "email TEXT," +
                "dia_chi TEXT," +
                "vai_tro INTEGER)"

        const val CREATE_TABLE_TRUYENDADOC = "CREATE TABLE TruyenDaDoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dadoc INTEGER," +
                "user_id INTEGER," +
                "Truyen_id INTEGER," +
                "FOREIGN KEY (user_id) REFERENCES user (id)," +
                "FOREIGN KEY (Truyen_id) REFERENCES Truyen(id))"

        const val CREATE_TABLE_TRUYENYEUTHICH = "CREATE TABLE TruyenYeuThich (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "yeuthich INTEGER," +
                "user_id INTEGER," +
                "Truyen_id INTEGER," +
                "FOREIGN KEY (user_id) REFERENCES user (id)," +
                "FOREIGN KEY (Truyen_id) REFERENCES Truyen(id))"

        const val CREATE_TABLE_TRUYENDB = "CREATE TABLE truyendb (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_category INTEGER," +
                "tentruyen TEXT," +
                "tac_gia TEXT," +
                "ngay_phat_hanh TEXT," +
                "mo_ta TEXT," +
                "linkhinh TEXT," +
                "luotxem INTEGER," +
                "luotyeuthich INTEGER," +
                "theloaitruyen TEXT," +
                "tinhtrangtruyen TEXT)"

        const val CREATE_TABLE_COMMENTS = "CREATE TABLE comments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "id_user INTEGER," +
                "comment TEXT," +
                "star INTEGER," +
                "id_truyen INTEGER," +
                "FOREIGN KEY (id_user) REFERENCES user (id)," +
                "FOREIGN KEY (id_truyen) REFERENCES truyendb (id))"

        const val CREATE_TABLE_CHAPTERDB = "CREATE TABLE chapterdb (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tenchapter TEXT," +
                "timechap TEXT," +
                "urlchapter TEXT," +
                "truyen_id INTEGER," +
                "FOREIGN KEY (truyen_id) REFERENCES truyen (id))"

        const val CREATE_TABLE_CHAPTERDADOC = "CREATE TABLE chapterdadoc (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Chapter_id INTEGER," +
                "TruyenDaDoc_id INTEGER," +
                "truyen_id INTEGER," +
                "FOREIGN KEY (Chapter_id) REFERENCES Chapter (id)," +
                "FOREIGN KEY (TruyenDaDoc_id) REFERENCES TruyenDaDoc (id) ON DELETE CASCADE," +
                "FOREIGN KEY (truyen_id) REFERENCES truyen (id))"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_TRUYEN)
        db?.execSQL(CREATE_TABLE_CHAPTER)
        db?.execSQL(CREATE_TABLE_USER)
        db?.execSQL(CREATE_TABLE_TRUYENDADOC)
        db?.execSQL(CREATE_TABLE_TRUYENDB)
        db?.execSQL(CREATE_TABLE_COMMENTS)
        db?.execSQL(CREATE_TABLE_CHAPTERDB)
        db?.execSQL(CREATE_TABLE_CHAPTERDADOC)
        db?.execSQL(CREATE_TABLE_CATEGORY)
        db?.execSQL(CREATE_TABLE_TRUYENYEUTHICH)
        db?.execSQL("INSERT INTO user (user_name, user, password, email, dia_chi, vai_tro) VALUES ('nang2002', 'Trac Nang', '20052002', null, null, 1)")
        db?.execSQL("INSERT INTO user (user_name, user, password, email, dia_chi, vai_tro) VALUES ('a', 'Người mới', '1', null, null, 1)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Handle database schema upgrades here
    }

    fun getAllUser(){
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM user", null)
        val idIndex = cursor.getColumnIndex("id")
        val userNameIndex = cursor.getColumnIndex("user_name")
        val userIndex = cursor.getColumnIndex("user")
        val passwordIndex = cursor.getColumnIndex("password")
        val emailIndex = cursor.getColumnIndex("email")
        val diaChiIndex = cursor.getColumnIndex("dia_chi")
        val vaiTroIndex = cursor.getColumnIndex("vai_tro")
        while (cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val userName = cursor.getString(userNameIndex)
            val user = cursor.getString(userIndex)
            val password = cursor.getString(passwordIndex)
            val email = cursor.getString(emailIndex)
            val diaChi = cursor.getString(diaChiIndex)
            val vaiTro = cursor.getInt(vaiTroIndex)
            println("id : $id, userName : $userName, user : $user, password : $password, email : $email, diaChi : $diaChi, vaiTro : $vaiTro")
        }
        cursor.close()
        db.close()
    }

    fun checklogin(user: String, password: String): Int {
        val ids = mutableListOf<Int>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM user WHERE user_name = ? AND password = ?", arrayOf(user, password))
        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex("id")
            val id = if (idIndex != -1) cursor.getInt(idIndex) else 0
            ids.add(id)
        }
        cursor.close()
        db.close()
        return ids.get(0)
    }

    //getUserByID
    fun getUserByID(id : Int) : String {
        val ids = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM user WHERE id = ?", arrayOf(id.toString()))

        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex("user")
            val user = if (idIndex != -1) cursor.getString(idIndex) else ""
            ids.add(user)
        }
        cursor.close()
        db.close()
        return ids.get(0)
    }

    //getTruyenDbbyId
    fun getTruyenDBByID(id : Int) : Truyen{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM truyendb WHERE id = ?", arrayOf(id.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val id_categoryIndex = cursor.getColumnIndex("id_category")
        val tentruyenIndex = cursor.getColumnIndex("tentruyen")
        val tac_giaIndex = cursor.getColumnIndex("tac_gia")
        val ngay_phat_hanhIndex = cursor.getColumnIndex("ngay_phat_hanh")
        val mo_taIndex = cursor.getColumnIndex("mo_ta")
        val linkhinhIndex = cursor.getColumnIndex("linkhinh")
        val luotxemIndex = cursor.getColumnIndex("luotxem")
        val luotyeuthichIndex = cursor.getColumnIndex("luotyeuthich")
        val theloaitruyenIndex = cursor.getColumnIndex("theloaitruyen")
        val tinhtrangtruyenIndex = cursor.getColumnIndex("tinhtrangtruyen")
        if(cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val id_category = cursor.getInt(id_categoryIndex)
            val tentruyen = cursor.getString(tentruyenIndex)
            val tac_gia = cursor.getString(tac_giaIndex)
            val ngay_phat_hanh = cursor.getString(ngay_phat_hanhIndex)
            val mo_ta = cursor.getString(mo_taIndex)
            val linkhinh = cursor.getString(linkhinhIndex)
            val luotxem = cursor.getInt(luotxemIndex)
            val luotyeuthich = cursor.getInt(luotyeuthichIndex)
            val theloaitruyen = cursor.getString(theloaitruyenIndex)
            val tinhtrangtruyen = cursor.getString(tinhtrangtruyenIndex)
            return Truyen(id, id_category, tentruyen, tac_gia, ngay_phat_hanh, mo_ta, linkhinh, luotxem, luotyeuthich, theloaitruyen, tinhtrangtruyen)
        }
        cursor.close()
        db.close()
        return Truyen(0,0,"","","","","",0,0,"","")
    }

    //getTruyenYeuThichByIDTruyenAndIDUser
    fun getTruyenYeuThichByIDTruyenAndIDUser(idTruyen :Int, idUser : Int) : TruyenYeuThich? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM TruyenYeuThich WHERE Truyen_id = ? AND user_id = ?", arrayOf(idTruyen.toString(), idUser.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val yeuthichIndex = cursor.getColumnIndex("yeuthich")
        val user_idIndex = cursor.getColumnIndex("user_id")
        val Truyen_idIndex = cursor.getColumnIndex("Truyen_id")
        if(cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val yeuthich = cursor.getInt(yeuthichIndex)
            val user_id = cursor.getInt(user_idIndex)
            val Truyen_id = cursor.getInt(Truyen_idIndex)
            return TruyenYeuThich(id, yeuthich, user_id, Truyen_id)
        }
        cursor.close()
        db.close()
        return null
    }

    //getAllTruyenDaDocByIDUser
    fun getAllTruyenDaDocByIDUser(idUser : Int) : List<TruyenDaDoc>{
        val listTruyenDaDoc = ArrayList<TruyenDaDoc>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM TruyenDaDoc WHERE user_id = ?", arrayOf(idUser.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val dadocIndex = cursor.getColumnIndex("dadoc")
        val user_idIndex = cursor.getColumnIndex("user_id")
        val Truyen_idIndex = cursor.getColumnIndex("Truyen_id")
        while (cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val dadoc = cursor.getInt(dadocIndex)
            val user_id = cursor.getInt(user_idIndex)
            val Truyen_id = cursor.getInt(Truyen_idIndex)
            listTruyenDaDoc.add(TruyenDaDoc(id, dadoc, user_id, Truyen_id))
        }
        cursor.close()
        db.close()
        return listTruyenDaDoc
    }

    //getIdChapter
    fun getIdChapter(tenchapter : String, idtruyen : Int) : Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM chapterdb WHERE tenchapter = ? AND truyen_id = ?", arrayOf(tenchapter, idtruyen.toString()))
        val idIndex = cursor.getColumnIndex("id")
        if(cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            return id
        }
        cursor.close()
        db.close()
        return 0
    }

    //getAllComment
    fun getAllComment(idTruyen : Int) : List<Comment>{
        val listComment = ArrayList<Comment>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM comments WHERE id_truyen = ?", arrayOf(idTruyen.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val id_userIndex = cursor.getColumnIndex("id_user")
        val commentIndex = cursor.getColumnIndex("comment")
        val starIndex = cursor.getColumnIndex("star")
        val id_truyenIndex = cursor.getColumnIndex("id_truyen")
        while (cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val id_user = cursor.getInt(id_userIndex)
            val comment = cursor.getString(commentIndex)
            val star = cursor.getInt(starIndex)
            val id_truyen = cursor.getInt(id_truyenIndex)
            listComment.add(Comment(id, id_user, comment, star, id_truyen))
        }
        cursor.close()
        db.close()
        return listComment
    }

    //getAllChapter
    fun getAllChapter(id : Int) : ArrayList<Chapter>{
        val listChapter = ArrayList<Chapter>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM chapterdb WHERE truyen_id = ?", arrayOf(id.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val tenchapterIndex = cursor.getColumnIndex("tenchapter")
        val timechapterIndex = cursor.getColumnIndex("timechap")
        val urlchapterIndex = cursor.getColumnIndex("urlchapter")
        val truyen_idIndex = cursor.getColumnIndex("truyen_id")
        while (cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val tenchapter = cursor.getString(tenchapterIndex)
            val timechapter = cursor.getString(timechapterIndex)
            val urlchapter = cursor.getString(urlchapterIndex)
            val truyen_id = cursor.getInt(truyen_idIndex)
            listChapter.add(Chapter(id, tenchapter, timechapter, urlchapter, truyen_id))
        }
        cursor.close()
        db.close()
        return listChapter
    }



    //getall
    fun getAllTruyen(): List<TruyenModel> {
        val listTruyen = ArrayList<TruyenModel>()
        val db = readableDatabase

        val cursor = db.rawQuery("SELECT * FROM truyen", null)
        val idIndex = cursor.getColumnIndex("id")
        val tentruyenIndex = cursor.getColumnIndex("tentruyen")
        val linkhinhIndex = cursor.getColumnIndex("linkhinh")
        val linktruyenIndex = cursor.getColumnIndex("linktruyen")

        while (cursor.moveToNext()) {
            val id = cursor.getInt(idIndex)
            val tentruyen = cursor.getString(tentruyenIndex)
            val linkhinh = cursor.getString(linkhinhIndex)
            val linktruyen = cursor.getString(linktruyenIndex)
            val chapters = ArrayList<ChapterModel>()
            val cursor1 = db.rawQuery("SELECT * FROM chapter WHERE truyenId = ?", arrayOf(id.toString()))
            val chapterIdIndex = cursor1.getColumnIndex("id")
            val tenchapIndex = cursor1.getColumnIndex("tenchap")
            val linkchapIndex = cursor1.getColumnIndex("linkchap")
            val timechapIndex = cursor1.getColumnIndex("timechap")

            while (cursor1.moveToNext()) {
                val chapterId = if (chapterIdIndex != -1) cursor1.getInt(chapterIdIndex) else 0
                val tenchap = if (tenchapIndex != -1) cursor1.getString(tenchapIndex) else ""
                val linkchap = if (linkchapIndex != -1) cursor1.getString(linkchapIndex) else ""
                val timechap = if (timechapIndex != -1) cursor1.getString(timechapIndex) else ""
                chapters.add(ChapterModel(chapterId, id, tenchap, linkchap, timechap))
            }

            cursor1.close()
            val truyen = TruyenModel(id, tentruyen, linkhinh, chapters, linktruyen)
            listTruyen.add(truyen)
        }

        cursor.close()
        db.close()
        return listTruyen
    }

    //countCommentInTruyenDB
    fun countCommentInTruyenDB(idTruyen : Int) : Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM comments WHERE id_truyen = ?", arrayOf(idTruyen.toString()))
        val count = cursor.count
        cursor.close()
        db.close()
        return count
    }

    //SumStarCommentInTruyenDB
    fun sumStarCommentInTruyenDB(idTruyen : Int) : Int{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM comments WHERE id_truyen = ?", arrayOf(idTruyen.toString()))
        val starIndex = cursor.getColumnIndex("star")
        var sum = 0
        while (cursor.moveToNext()){
            val star = cursor.getInt(starIndex)
            sum += star
        }
        cursor.close()
        db.close()
        return sum
    }

    //getChappterDaDocByIDChapterAndIDTruyenAndIDUser
    fun getChappterDaDocByIDChapterAndIDTruyenAndIDUser(idChapter : Int, idTruyen : Int, TruyenDaDoc_id : Int) : ChapterDaDoc?{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM chapterdadoc WHERE Chapter_id = ? AND truyen_id = ? AND TruyenDaDoc_id = ?", arrayOf(idChapter.toString(), idTruyen.toString(), TruyenDaDoc_id.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val Chapter_idIndex = cursor.getColumnIndex("Chapter_id")
        val TruyenDaDoc_idIndex = cursor.getColumnIndex("TruyenDaDoc_id")
        val truyen_idIndex = cursor.getColumnIndex("truyen_id")
        if(cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val Chapter_id = cursor.getInt(Chapter_idIndex)
            val TruyenDaDoc_id = cursor.getInt(TruyenDaDoc_idIndex)
            val truyen_id = cursor.getInt(truyen_idIndex)
            return ChapterDaDoc(id, Chapter_id, TruyenDaDoc_id, truyen_id)
        }
        cursor.close()
        db.close()
        return null
    }



    //getTruyenDaDocByIDTruyenAndIDUser
    fun getTruyenDaDocByIDTruyenAndIDUser(idTruyen : Int, idUser : Int) : TruyenDaDoc?{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM TruyenDaDoc WHERE Truyen_id = ? AND user_id = ?", arrayOf(idTruyen.toString(), idUser.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val dadocIndex = cursor.getColumnIndex("dadoc")
        val user_idIndex = cursor.getColumnIndex("user_id")
        val Truyen_idIndex = cursor.getColumnIndex("Truyen_id")
        if(cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val dadoc = cursor.getInt(dadocIndex)
            val user_id = cursor.getInt(user_idIndex)
            val Truyen_id = cursor.getInt(Truyen_idIndex)
            return TruyenDaDoc(id, dadoc, user_id, Truyen_id)
        }
        cursor.close()
        db.close()
        return null
    }

    //insertTruyenDaDoc
    fun insertTruyenDaDoc(truyenDaDoc: TruyenDaDoc) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("dadoc", 1)
        values.put("user_id", truyenDaDoc.user_id)
        values.put("Truyen_id", truyenDaDoc.Truyen_id)
        db.insert("TruyenDaDoc", null, values)
        db.close()
    }
    //insertChapterDaDoc
    fun insertChapterDaDoc(chapterDaDoc: ChapterDaDoc) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("Chapter_id", chapterDaDoc.Chapter_id)
        values.put("TruyenDaDoc_id", chapterDaDoc.TruyenDaDoc_id)
        values.put("truyen_id", chapterDaDoc.truyen_id)
        db.insert("chapterdadoc", null, values)
        db.close()
    }



    //insert
    fun insertTruyen(truyen: TruyenModel) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("id", truyen.id)
        values.put("tentruyen", truyen.tentruyen)
        values.put("linkhinh", truyen.linkhinhtruyen)
        values.put("linktruyen", truyen.linktruyen)
        val id = db.insert("truyen", null, values)
        truyen.chaptruyen.forEach {
            val values1 = ContentValues()
            values1.put("truyenId", id)
            values1.put("tenchap", it.tenchap)
            values1.put("linkchap", it.linkchap)
            values1.put("timechap", it.timechap)
            db.insert("chapter", null, values1)
        }
        db.close()
    }

    //insertChappter
    fun insertChapter(chapter: Chapter) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("truyen_id", chapter.truyen_id)
        values.put("tenchapter", chapter.tenchapter)
        values.put("urlchapter", chapter.urlchapter)
        values.put("timechap", chapter.timechapter)
        db.insert("chapterdb", null, values)
        println("da thanh cong")
        db.close()
    }
    //insertTruyenYeuThich
    fun insertTruyenYeuThich(truyenYeuThich: TruyenYeuThich) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("yeuthich", truyenYeuThich.yeuthich)
        values.put("user_id", truyenYeuThich.user_id)
        values.put("Truyen_id", truyenYeuThich.Truyen_id)
        db.insert("TruyenYeuThich", null, values)
        db.close()
    }

    //insertComment
    fun insertComment(comment: Comment) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("id_user", comment.user_id)
        values.put("comment", comment.comment)
        values.put("star", comment.star)
        values.put("id_truyen", comment.id_truyen)
        db.insert("comments", null, values)
        db.close()
    }

    //insertTruyen
    fun insertTruyen(truyen : Truyen){
        val db = writableDatabase
        val values = ContentValues()
        values.put("id", truyen.id)
        values.put("id_category", truyen.id_category)
        values.put("tentruyen", truyen.tentruyen)
        values.put("tac_gia", truyen.tac_gia)
        values.put("ngay_phat_hanh", truyen.ngay_phat_hanh)
        values.put("mo_ta", truyen.mo_ta)
        values.put("linkhinh", truyen.linkhinh)
        values.put("luotxem", truyen.luotxem)
        values.put("luotyeuthich", truyen.luotyeuthich)
        values.put("theloaitruyen", truyen.theloaitruyen)
        values.put("tinhtrangtruyen", truyen.tinhtrangtruyen)
        db.insert("truyendb", null, values)
        db.close()
    }

    //findByTenChapter
    fun findChapterByTenChapter(tenchapter : String, idtruyen : Int) : ChapterModel?{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM chapterdb WHERE  tenchapter = ? AND truyen_id = ?", arrayOf(tenchapter, idtruyen.toString()))
        val idIndex = cursor.getColumnIndex("id")
        val truyenIdIndex = cursor.getColumnIndex("truyen_id")
        val tenchapIndex = cursor.getColumnIndex("tenchapter")
        val linkchapIndex = cursor.getColumnIndex("urlchapter")
        val timechapIndex = cursor.getColumnIndex("timechap")
        if(cursor.moveToNext()){
            val id = cursor.getInt(idIndex)
            val truyenId = cursor.getInt(truyenIdIndex)
            val tenchap = cursor.getString(tenchapIndex)
            val linkchap = cursor.getString(linkchapIndex)
            val timechap = cursor.getString(timechapIndex)
            return ChapterModel(id, truyenId, tenchap, linkchap, timechap)
        }
        cursor.close()
        db.close()
        return null
    }

    //findbyId
    fun findTruyenById(id : Int) : Boolean{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM truyendb WHERE id = ?", arrayOf(id.toString()))
        val result = cursor.count > 0
        cursor.close()
        db.close()
        return result
    }




    //findbyLinkTruyen
    fun findTruyenByLink(link: String): TruyenModel? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM truyen WHERE linktruyen = ?", arrayOf(link))
        val idIndex = cursor.getColumnIndex("id")
        val tentruyenIndex = cursor.getColumnIndex("tentruyen")
        val linkhinhIndex = cursor.getColumnIndex("linkhinh")
        val linktruyenIndex = cursor.getColumnIndex("linktruyen")

        if (cursor.moveToNext()) {
            val id = cursor.getInt(idIndex)
            val tentruyen = cursor.getString(tentruyenIndex)
            val linkhinh = cursor.getString(linkhinhIndex)
            val linktruyen = cursor.getString(linktruyenIndex)
            val chapters = ArrayList<ChapterModel>()
            val cursor1 = db.rawQuery("SELECT * FROM chapter WHERE truyenId = ?", arrayOf(id.toString()))
            val chapterIdIndex = cursor1.getColumnIndex("id")
            val tenchapIndex = cursor1.getColumnIndex("tenchap")
            val linkchapIndex = cursor1.getColumnIndex("linkchap")
            val timechapIndex = cursor1.getColumnIndex("timechap")

            while (cursor1.moveToNext()) {
                val chapterId = if (chapterIdIndex != -1) cursor1.getInt(chapterIdIndex) else 0
                val tenchap = if (tenchapIndex != -1) cursor1.getString(tenchapIndex) else ""
                val linkchap = if (linkchapIndex != -1) cursor1.getString(linkchapIndex) else ""
                val timechap = if (timechapIndex != -1) cursor1.getString(timechapIndex) else ""
                chapters.add(ChapterModel(chapterId, id, tenchap, linkchap, timechap))
            }

            cursor1.close()
            val truyen = TruyenModel(id, tentruyen, linkhinh, chapters, linktruyen)
            cursor.close()
            db.close()
            return truyen
        }
        cursor.close()
        db.close()
        return null
    }

    //updateLuotXem
    fun updateLuotXem(id : Int, luotxem : Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("luotxem", luotxem)

        db.update("truyendb", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }

    //updateLuotYeuThich
    fun updateLuotYeuThich(id : Int, luotyeuthich : Int) {
        val db = writableDatabase
        val values = ContentValues()
        values.put("luotyeuthich", luotyeuthich)
        db.update("truyendb", values, "id = ?", arrayOf(id.toString()))
        db.close()
    }

    //updateTruyenYeuThich
    fun updateTruyenYeuThich(idTruyen : Int, idUser : Int, yeuthich : Int){
        val db = writableDatabase
        val values = ContentValues()
        values.put("yeuthich", yeuthich)
        db.update("TruyenYeuThich", values, "Truyen_id = ? AND user_id = ?", arrayOf(idTruyen.toString(), idUser.toString()))
        db.close()
    }


    //remove
    fun removeTruyen(truyen: TruyenModel) {
        val db = writableDatabase
        db.delete("truyen", "id = ?", arrayOf(truyen.id.toString()))
        db.delete("chapter", "truyenId = ?", arrayOf(truyen.id.toString()))
        db.close()
    }

    //removeLichSuTruyenByID
    fun removeLichSuTruyenByID(id : Int){
        val db = writableDatabase
        db.delete("TruyenDaDoc", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}
