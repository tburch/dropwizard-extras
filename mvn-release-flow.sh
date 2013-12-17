#!/bin/sh
#
# Maven release using gitflow stuff
# from https://github.com/vdemeester/java-config/blob/master/bin/mvn-release-flow, based on http://vincent.demeester.fr/2012/07/maven-release-gitflow/ and https://gist.github.com/searls/1043970
#
# 0. verify the precense of needed command (git, mvn, …)
# 1. get the stable version to come (but let me specify it)
# 2. do your stuff (create the branch, ...)

# The most important line in each script
set -e

# Check for the git command
command -v git >/dev/null || {
    echo "git command not found." 1>&2
    exit 1
}
# Check for the mvn command (maven)
command -v mvn >/dev/null || {
    echo "mvn command not found." 1>&2
    exit 1
}

get_release_branch() {
    echo "release/$1"
}

create_release_branch() {
    echo "Creating release branch... "
    RELEASE_BRANCH=$(get_release_branch $1)
    echo "Created release branch will be $RELEASE_BRANCH "
    create_branch=$(git checkout -b ${RELEASE_BRANCH} develop 2>&1)
    echo "Using release branch $RELEASE_BRANCH "
}

remove_release_branch() {
    echo "Removing release branch... "
    RELEASE_BRANCH=$(get_release_branch $1)
    echo "Release branch to delete is $RELEASE_BRANCH "
    remove_branch=$(git branch -D ${RELEASE_BRANCH} 2>&1)
    echo "Deleted release branch $RELEASE_BRANCH "
}

mvn_release() {
    echo "Using maven-release-plugin... "
    mvn_release_clean=$(mvn release:clean --settings ~/.m2/mySettings.xml -B)
    echo "Running 'mvn release:clean' "
    mvn_release_prepare=$(mvn release:prepare --settings ~/.m2/mySettings.xml -B)
    echo "Running 'mvn release:prepare' "
    mvn_release_perform=$(mvn release:perform --settings ~/.m2/mySettings.xml -B)
    echo "Running 'mvn release:perform' "
}

# Merging the content of release branch to develop
merging_to_develop() {
    echo "Merging back to develop... "
    RELEASE_BRANCH=$(get_release_branch $1)
    git_co_develop=$(git checkout develop 2>&1)
    git_merge=$(git merge --no-ff ${RELEASE_BRANCH} 2>&1)
    echo "Completed merging back to develop. "
}

# Rewind 1 commit (tag)
# Merge master in release_branch with ours strategy (we have the sure one)
# Merge back release_branch to master
merging_to_master() {
    echo "Merging back to master... "
    RELEASE_BRANCH=$(get_release_branch $1)
    git_co_release_branch=$(git checkout ${RELEASE_BRANCH})
    git_resete_release_branch=$(git reset --hard HEAD~1)
    git_merge_ours=$(git merge -s ours master 2>&1)
    git_co_master=$(git checkout master 2>&1)
    # We make the assumption "theirs" is the best
    git_merge=$(git merge --no-ff ${RELEASE_BRANCH} 2>&1)
    echo "Completed merging back to master. "
}

# First get the working directory
echo "Detecting version number... "
if `git rev-parse 2>/dev/null`; then
    BRANCH_NAME=$(git symbolic-ref -q HEAD)
    BRANCH_NAME=${BRANCH_NAME##refs/heads/}
    WORKING_DIR=$(git rev-parse --show-toplevel)
    cd $WORKING_DIR
    CURRENT_VERSION=`mvn --settings ~/.m2/mySettings.xml org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version | grep -v '^\['`
    if test "$CURRENT_VERSION" = "${CURRENT_VERSION%-SNAPSHOT}"; then
        echo "version '${CURRENT_VERSION}' specified is not a snapshot"
    else
        STABLE_VERSION="${CURRENT_VERSION%-SNAPSHOT}"
        echo "Stable version is $STABLE_VERSION"
        create_release_branch $STABLE_VERSION
        mvn_release
        merging_to_develop $STABLE_VERSION
        merging_to_master $STABLE_VERSION
        remove_release_branch $STABLE_VERSION
        # Getting back to where we were
        git checkout $BRANCH_NAME
    fi
else
    echo "you are not in a git directory"
    exit 2
fi

