package fr.ftnl.lang

import com.google.gson.JsonObject
import fr.ftnl.GSON
import java.io.File

/**
 * Manage fr.ftnl.lang files
 * @property lang [String] fr.ftnl.lang code
 * @property data [JsonObject] fr.ftnl.lang data
 */
class LangFile(private val lang : String, data : JsonObject) {
	var data : JsonObject = data
		set(value) {
			field = value
			save()
		}
	
	private fun save() = File("./lang/$lang.json").writeText(GSON.toJson(data))
	
	companion object {
		private val LANG_DIR = File("./lang/")
		
		init {
			LANG_DIR.mkdirs()
		}
		
		/**
		 * Load fr.ftnl.lang file
		 * @param lang [String] fr.ftnl.lang code
		 */
		fun load(lang : String) : LangFile {
			val file = File(LANG_DIR, "$lang.json")
			if (!file.exists()) file.createNewFile()
			val data = GSON.fromJson(file.readText(), JsonObject::class.java)
				?: JsonObject()
			return LangFile(lang, data)
		}
	}
}