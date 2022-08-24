package fr.ftnl

import com.google.gson.GsonBuilder
import dev.minn.jda.ktx.events.CoroutineEventListener
import dev.minn.jda.ktx.jdabuilder.injectKTX
import dev.minn.jda.ktx.messages.Embed
import fr.ftnl.lang.LangKey
import fr.ftnl.lang.LangLoader
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.DiscordLocale
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.utils.MemberCachePolicy
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder

val GSON = GsonBuilder()
	.setPrettyPrinting()
	.create()

val lang = LangLoader()
val memeList = listOf(
		"https://cdn.discordapp.com/attachments/620218482654576640/1011735478858424453/unknown.png",
		"https://cdn.discordapp.com/attachments/620218482654576640/1011735478493523978/ripclassic.png"
)
var commandSend = false


fun main(args : Array<String>) {
	DefaultShardManagerBuilder
		.create(GatewayIntent.getIntents(0))
		.setToken(System.getenv("INVITE_LOGGER_DISCORD_TOKEN"))
		.setAutoReconnect(true)
		.disableCache(CacheFlag.values().toList())
		.setMemberCachePolicy(MemberCachePolicy.NONE)
		.addEventListeners(BotListener())
		.injectKTX()
		.build()
}

class BotListener: CoroutineEventListener {
	override suspend fun onEvent(event : GenericEvent) {
		when(event){
			is SlashCommandInteractionEvent -> {
				val manager = lang.getLangManager(event.userLocale)
				val embed = Embed {
					title = manager
						.getString(LangKey.keyBuilder(this, "slash", "embedTitle"),
								   "It's time",
						)
					description = manager
						.getString(LangKey.keyBuilder(this, "slash", "embedDescription"),
								   """
									  After two great years of good and loyal service, it is time for me to leave you.

									  InviteLogger Classic is terminating these services as of <t:1661983200:D>
									  This decision was made on the basis of several factors:
								   """.trimIndent()
						)
					
					field {
						title = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField1Title"),
									   "It's a fork :")
						
						value = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField1Value"),
									   """
										   InviteLogger classic is a fork of an almost dead open-source project,
										   it succeeded the InviteManager bot by taking over its code base.
								   """.trimIndent()
							)
						inline = false
					}
					field {
						title = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField2Title"),
									   "It has a non-negligible cost :")
						
						value = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField2Value"),
									   """
										   InviteLogger classic consumes a large amount of resources,
										   it is currently a large financial sinkhole the bot being completely free.
								   """.trimIndent()
							)
						inline = false
					}
					field {
						title = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField3Title"),
									   "Message content intent :")
						
						value = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField3Value"),
									   """
										   As of the end date of InviteLogger classic,
										   Discord puts a limitation for bots regarding access to message content,
										   it would be too much work to adapt the bot to slash commands,
										   this reason is the one that has finished hammering the nail in the coffin.
								   """.trimIndent()
							)
						inline = false
					}
					field {
						title = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField4Title"),
									   "Some statistics :")
						
						value = manager
							.getString(LangKey.keyBuilder(this, "slash", "embedField4Value"),
									   """
										   InviteLogger Classic tracked 155 million logins from 46 million invitees,
										   fulfilled 15 million orders,
										   viewed 42 million invite codes and visited 212,000 servers,
										   for a database of about 100 GB.
								   """.trimIndent()
							)
						inline = false
					}
					
					image = memeList.random()
				}
				
				val message = MessageCreateBuilder()
					.setEmbeds(embed)
					.addActionRow(
							Button.link(
									manager.getString(
											LangKey.keyBuilder(this, "slash", "supportButtonLink"),
											"https://discord.gg/eDQuKsFnSd"
									),
									manager.getString(
											LangKey.keyBuilder(this, "slash", "supportButtonText"),
											""
									)
							).withEmoji(Emoji.fromCustom("discord", 620220553793503274L, true)),
							
							Button.link(
									manager.getString(
											LangKey.keyBuilder(this, "slash", "siteButtonLink"),
											"https://invitelogger.me/"
									),
									manager.getString(
											LangKey.keyBuilder(this, "slash", "siteButtonText"),
											"My website"
									)
							).withEmoji(Emoji.fromCustom("invlog_sheep", 763833470232559626L, false))
					)
					.build()
				
				event.reply(message)
					.setEphemeral(event.member?.hasPermission(Permission.MESSAGE_MANAGE) == false)
					.queue()
			}
			is ReadyEvent -> {
				if (commandSend) return
				commandSend = true
				val command = Commands
					.slash("info", "InviteLogger classic has ended its life")
					.setLocalizationFunction {
						val element = it.replace(".", "+")
						val cmdLocale = DiscordLocale
							.values().toMutableList()
						cmdLocale.remove(DiscordLocale.UNKNOWN)
						return@setLocalizationFunction cmdLocale.associateWith { locale ->
							lang.getLangManager(locale).getString(
									LangKey.keyBuilder(this, "commandLocalization", element),
									it.split(".")[0]
							)
						}
					}
				event.jda.updateCommands().addCommands(command).queue()
			}
		}
	}
}