Upgrade notes: Java 21 (LTS)

This project has been updated to target Java 21.

What I changed:
- `pom.xml`: set `<java.version>21</java.version>` and added `maven-compiler-plugin` and `maven-enforcer-plugin` to require JDK >= 21 at build time.

Why you may see a build failure:
- The Maven Enforcer plugin now requires JDK 21+. If your system is using Java 17 (or older), the build will fail early with a clear message.

How to finish the upgrade (PowerShell commands):

1) Install a JDK 21 distribution (choose one):
- Using winget (recommended on Windows 10/11):
  # Try Eclipse Temurin (Adoptium)
  winget install --id EclipseAdoptium.Temurin.21.JDK -e

  # Or Microsoft Build of OpenJDK 21
  winget install --id Microsoft.OpenJDK.21 -e

- Manual download (if winget not available):
  - Temurin: https://adoptium.net/
  - Microsoft Build of OpenJDK: https://learn.microsoft.com/en-us/java/openjdk/

2) For the current PowerShell session, set JAVA_HOME and update PATH (replace path below with the actual install path on your machine):

  $Env:JAVA_HOME = 'C:\Program Files\Microsoft\jdk-21'   # or the path where your JDK 21 is installed
  $Env:Path = "$Env:JAVA_HOME\bin;$Env:Path"

3) Verify Java and Maven use JDK 21:

  .\mvnw -v
  java -version

4) Build the project (skip tests for faster feedback):

  .\mvnw -DskipTests package

5) If everything builds, run full test suite:

  .\mvnw test

CI / Docker / Other environments:
- Update CI runners to use JDK 21 (for GitHub Actions use `actions/setup-java` with `java-version: '21'`).
- Update any Dockerfiles that install a JDK to use a Java 21 base image.

Notes & next steps I can help with:
- Update CI configuration to use Java 21.
- Run the full build/tests and fix any compatibility issues arising from the JDK bump.
- Audit dependencies for Java 21 compatibility and update versions if needed.

If you want, I can try to install JDK 21 on this machine (requires admin and availability of the system package manager). I attempted to use the built-in Java upgrade tool but it requires a different Copilot plan, so I updated the project files and provided the steps above.
