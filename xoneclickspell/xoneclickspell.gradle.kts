version = "1.0.1"

project.extra["PluginName"] = "XOneClickSpell"
project.extra["PluginDescription"] = "Auto cast spell on your enemy with a hotkey"

tasks {
	jar {
		manifest {
			attributes(mapOf(
					"Plugin-Version" to project.version,
					"Plugin-Id" to nameToId(project.extra["PluginName"] as String),
					"Plugin-Provider" to project.extra["PluginProvider"],
					"Plugin-Dependencies" to
						arrayOf(
							nameToId("iUtils")
						).joinToString(),
					"Plugin-Description" to project.extra["PluginDescription"],
					"Plugin-License" to project.extra["PluginLicense"]
			))
		}
	}
}
