#!/usr/bin/env bash
SELENOID_1=`docker inspect selenoid -f {{.NetworkSettings.Networks.selenoid.Gateway}}`
SELENOID_2=`docker inspect selenoid2 -f {{.NetworkSettings.Networks.selenoid2.Gateway}}`
SELENOID_1_PORT=4445
SELENOID_2_PORT=4446

echo $SELENOID_1 ' = new selenoid ip address'
echo $SELENOID_2 ' = new selenoid2 ip address'

GGR_CONF_PATH="$PWD/etc/grid-router/quota/test.xml"

# $1 = browser_name
# $2 = default_version
# $3 = selenoid_ip
# $4 = selenoid_port
print_browser_settings () {
cat >> "${GGR_CONF_PATH}" <<- EOM
    <browser name="$1" defaultVersion="$2">
        <version number="$2">
            <region name="1">
                <host name="$3" port="$4" count="1"/>
            </region>
        </version>
    </browser>
EOM
}

#cat > "${GGR_CONF_PATH}" <<- EOM
#<qa:browsers xmlns:qa="urn:config.gridrouter.qatools.ru">
#EOM
#print_browser_settings android 5.1 $SELENOID_1 $SELENOID_1_PORT
#print_browser_settings chrome 121.0 $SELENOID_1 $SELENOID_1_PORT
#print_browser_settings chrome 120.0 $SELENOID_2 $SELENOID_2_PORT
#cat >> "${GGR_CONF_PATH}" <<- EOM
#</qa:browsers>
#EOM

cat > "${GGR_CONF_PATH}" <<- EOM
<qa:browsers xmlns:qa="urn:config.gridrouter.qatools.ru">
    <browser name="android" defaultVersion="5.1">
        <version number="5.1">
            <region name="1">
                <host name="$SELENOID_1" port="$SELENOID_1_PORT" count="1"/>
            </region>
        </version>
    </browser>
    <browser name="chrome" defaultVersion="121.0">
        <version number="121.0">
            <region name="1">
                <host name="$SELENOID_1" port="$SELENOID_1_PORT" count="1"/>
            </region>
        </version>
        <version number="120.0">
            <region>
                <host name="$SELENOID_2" port="$SELENOID_2_PORT" count="1"/>
            </region>
        </version>
    </browser>
    <browser name="opera" defaultVersion="106.0">
        <version number="106.0">
            <region name="1">
                <host name="$SELENOID_1" port="$SELENOID_1_PORT" count="1"/>
            </region>
        </version>
        <version number="105.0">
            <region>
                <host name="$SELENOID_2" port="$SELENOID_2_PORT" count="1"/>
            </region>
        </version>
    </browser>
</qa:browsers>
EOM