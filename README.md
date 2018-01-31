# devtest-general-ext v0.0.1-SNAPSHOT
CA DevTest extensions that are generally useful for Service Virtualization.

**Author:** Mike Gavaghan - **Email:** mike@gavaghan.org

## DevTest Application Dependencies
    com.ca.devtest-?.?

## Private Dependencies ##
This artifact has two private dependencies:

    gavaghan-json-1.0.1
    devtest-maven-plugin-0.2.0

One approach is to simply build and install these artifacts locally after cloning the git repositories.  However, until proper branching is implemented on these repos, you can only build against the latest-and-greatest.

Alternatively, you can point to the private DevTest repository.  Please add this to your `settings.xml` file:

	<!-- Remote repository for DevTest artifacts -->
	<repositories>
		<repository>
			<id>private-devtest-repo</id>
			<name>Private DevTest Repository</name>
			<releases>
				<enabled>true</enabled>
				<updatePolicy>never</updatePolicy>
		 		<checksumPolicy>fail</checksumPolicy>
			</releases>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
			<url>http://www.gavaghan.org/maven</url>
			<layout>default</layout>
		</repository>
	</repositories>

    <!-- Remote repository for DevTest plugins -->
	<pluginRepositories>
		<pluginRepository>
			<id>private-devtest-repo</id>
			<name>Private DevTest Repository</name>
			<releases>
				<enabled>true</enabled>
				<updatePolicy>never</updatePolicy>
				<checksumPolicy>fail</checksumPolicy>
			</releases>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
			<url>http://www.gavaghan.org/maven</url>
			<layout>default</layout>
		</pluginRepository>
	</pluginRepositories>

# Available Extensions #

