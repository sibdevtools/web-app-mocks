import com.github.gradle.node.npm.task.NpmTask

plugins {
  id("com.github.node-gradle.node") version "7.0.2"
}

node {
  version.set("22.9.0")
  npmVersion.set("10.8.3")
  download.set(true)
}

tasks.named<NpmTask>("npmInstall") {
  args.set(listOf("install"))
}

tasks.register<NpmTask>("buildFrontend") {
  dependsOn("npmInstall")
  args.set(listOf("run", "build"))
}
