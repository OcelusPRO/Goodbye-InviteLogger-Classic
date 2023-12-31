package fr.ftnl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.jdabuilder.injectKTX
import dev.minn.jda.ktx.messages.Embed
import fr.ftnl.lang.LangKey
import fr.ftnl.lang.LangLoader
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.session.ReadyEvent
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder

val GSON : Gson = GsonBuilder().setPrettyPrinting().create()
val lang = LangLoader()


fun main() {
	DefaultShardManagerBuilder.create(GatewayIntent.getIntents(0))
		.setToken(System.getenv("FTNL_DISCORD_TOKEN"))
		.setAutoReconnect(true)
		.disableCache(CacheFlag.values().toList())
		.setMemberCachePolicy(MemberCachePolicy.NONE)
		.addEventListeners(BotListener())
		.injectKTX()
		.build()
}

class BotListener : CoroutineEventListener {
	override suspend fun onEvent(event : GenericEvent) {
		when (event) {
			is SlashCommandInteractionEvent -> {
				val manager = lang.getLangManager(event.userLocale)
				val embed = Embed {
					title = manager.getString(
						LangKey.keyBuilder(this@BotListener, "embed", "title"),
						"Il est temps",
					)
					description = manager.getString(
						LangKey.keyBuilder(this@BotListener, "embed", "description"), """
							Après 6 ans de bons et loyaux services, il est temps de se dire au revoir...
							Le bot FTNL termine donc son activité au <t:1704063600:f>
						""".trimIndent()
					)
					
					field {
						name = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field1", "title"), "Une forte pression"
						)
						
						value = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field1", "value"), """
										FTNL a été pour moi (ocelus, son créateur) une très forte pression ces dernières années, une crainte constante de décevoir les utilisateurs, de devoir gérer toute une équipe, ou encore les sommes astronomiques que le bot m'a coûtées... J'ai besoin de faire une pause et de me détacher au moins en partie de toute cette pression.
								   """.trimIndent()
						)
						inline = false
					}
					field {
						name = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field2", "title"),
							"Un cout non négligable :"
						)
						
						value = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field2", "value"), """
								FTNL a toujours été entièrement gratuit, les quelques donations (extrêmement rares) ne suffisaient clairement pas à maintenir l'équilibre financier.
								L'hébergement du bot coûtait en moyenne 70 € tous les mois et ce, ces six dernières années, ce qui représente plusieurs milliers d'euros dépensés pour le maintenir.
								Ces derniers mois, même avec la fin annoncée, j'avais peur de ne pas réussir à payer l'hébergement.
							""".trimIndent()
						)
						inline = false
					}
					field {
						name = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field3", "title"), "Et maintenant ?"
						)
						
						value = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field3", "value"), """
										FTNL n'est pas destiné à avoir un arrêt définitif, cependant, cet arrêt va sans doute durer plusieurs années.
										Une solution alternative a été mise en place pour les serveurs pouvant se le permettre : un abonnement est disponible pour un successeur à FTNL :
										- [FTNL-Legacy](https://discord.com/api/oauth2/authorize?client_id=575642702180253696&permissions=8&scope=applications.commands%20bot)
										Ce dernier reprend l'ensemble des fonctions de FTNL (celles de FTNL-PRO lui seront ajoutées avec le temps) ainsi que quelques fonctions exclusives. Cependant, ce bot est payant et ne contiendra pas la fonction de blacklist devenue obsolète avec le temps et l'amélioration constante de nos moyens de détection des raids/spam.
								   """.trimIndent()
						)
						inline = false
					}
					field {
						name = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field4", "title"), "Quelques statistiques :"
						)
						
						value = manager.getString(
							LangKey.keyBuilder(this@BotListener, "embed.field4", "value"), """
										   Plus de 365 000 tentatives de spam ont été bloquées par FTNL, ce qui fait en moyenne 166,6 par jour !
										   175 276, le nombre d'ajouts et de retraits du bot sur des serveurs. Il en aura vu du paysage même s'il n'est pas resté partout.
										   Plus de 20 000 lignes de code ! (ça fait beaucoup)
										   
										   
										   Encore un grand merci à tous ceux qui ont soutenu FTNL, [ce n'est qu'un au revoir](https://www.youtube.com/watch?v=2X_FpnZ4k1c) !
								   """.trimIndent()
						)
						inline = false
					}
					
					image = "https://media2.giphy.com/media/hmxZRW8mhs4ak/giphy.gif"
				}
				
				val message = MessageCreateBuilder().setEmbeds(embed).addActionRow(
					Button.link(
						manager.getString(
							LangKey.keyBuilder(this@BotListener, "buttons.discord", "link"),
							"https://discord.gg/WYeeZS339B"
						), manager.getString(
							LangKey.keyBuilder(this@BotListener, "buttons.discord", "label"),
							"Serveur communautaire"
						)
					).withEmoji(Emoji.fromCustom("discord", 620220553793503274L, true)),
					Button.link(
						manager.getString(
							LangKey.keyBuilder(this@BotListener, "buttons.discordEntreprise", "link"),
							"https://discord.gg/JY9YQCdd4X"
						), manager.getString(
							LangKey.keyBuilder(this@BotListener, "buttons.discordEntreprise", "label"),
							"Serveur du nouveau bot"
						)
					).withEmoji(Emoji.fromCustom("discord", 620220553793503274L, true))
				).build()
				
				event.reply(message).setEphemeral(event.member?.hasPermission(Permission.MESSAGE_MANAGE) == false).queue()
			}
			
			is ReadyEvent -> {
				refreshPresence(event.jda)
			}
		}
	}
	
	private fun refreshPresence(jda: JDA) =
		jda.presence.setPresence(OnlineStatus.INVISIBLE, Activity.watching("La fin d'une époque"))
}