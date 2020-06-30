if [ -z "$1" ]
then
	echo "Version needs to be provided."
	echo "./update-pom-version.sh 1.0-SNAPSHOT"
	exit 0
fi

echo "updating version to: " $1
mvn versions:set -DnewVersion=$1
mvn versions:commit
