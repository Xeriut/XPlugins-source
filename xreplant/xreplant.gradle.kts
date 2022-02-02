version = "1.0.2"

project.extra["PluginName"] = "XReplant"
project.extra["PluginDescription"] = "Replants farming patches for you."

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
