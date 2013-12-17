#!/usr/bin/env python
import sys
import os
import os.path
import xml.dom.minidom

if os.environ["TRAVIS_SECURE_ENV_VARS"] == "false":
    print "no secure env vars available, skipping deployment"
    sys.exit()

def createAndAppendServerNode(serverId):
    serversNodes = settings.getElementsByTagName("servers")
    if not serversNodes:
        serversNode = m2.createElement("servers")
        settings.appendChild(serversNode)
    else:
        serversNode = serversNodes[0]

    serverNode = m2.createElement("server")
    sonatypeServerId = m2.createElement("id")
    sonatypeServerUser = m2.createElement("username")
    sonatypeServerPass = m2.createElement("password")

    idNode = m2.createTextNode(serverId)
    userNode = m2.createTextNode(os.environ["SONATYPE_USERNAME"])
    passNode = m2.createTextNode(os.environ["SONATYPE_PASSWORD"])

    sonatypeServerId.appendChild(idNode)
    sonatypeServerUser.appendChild(userNode)
    sonatypeServerPass.appendChild(passNode)

    serverNode.appendChild(sonatypeServerId)
    serverNode.appendChild(sonatypeServerUser)
    serverNode.appendChild(sonatypeServerPass)

    serversNode.appendChild(serverNode)

homedir = os.path.expanduser("~")

m2 = xml.dom.minidom.parse(homedir + '/.m2/settings.xml')
settings = m2.getElementsByTagName("settings")[0]

createAndAppendServerNode("sonatype-nexus-snapshots")
createAndAppendServerNode("sonatype-nexus-staging")

m2Str = m2.toxml()
f = open(homedir + '/.m2/mySettings.xml', 'w')
f.write(m2Str)
f.close()