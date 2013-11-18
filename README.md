# README
All you need to know about http://ifcfg.net/

# Why, Who, and Source
I wanted a simple to use API to get my current IP and other network information. Other websites that used to offer this have become cluttered with ads or taking multiple seconds to respond. I also wanted an excuse to make a small project to learn Scala and the Play! framework.

My name is Josh Rendek.

The source for this website is on github: https://github.com/joshrendek/scala-ifcfg-api

The BGP Looking Glass scraper library I made is here: https://github.com/joshrendek/scala-bgp

# API Endpoints

## GET /

Hitting the root url will echo back your IP.
Response

11.22.33.44

## GET /as/:num

Get information about a AS.
Response

{
    "bgpPeerList":4158,
    "as":174,
    "asPathLength":3.64,
    "ipsOriginated":27981312,
    "countryOfOrigin":"United States",
    "prefixList":2199
}

## GET /as/:num/peer_list

Get the list of peers for this AS.
Response

[
    {
        as: "AS3356",
        name: "Level 3 Communications, Inc.",
        ipv6: true,
        rank: 1
    }
]

## GET /as/:num/prefix_list

Get the list of prefixes contained within the AS.
Response

[
    {
        description: "Eurona-Brisknet Ltd",
        subnet: "5.83.240.0/20"
    },
    {
        description: "FORTUNIX NETWORKS L.P.",
        subnet: "5.149.252.0/23"
    }
]

## GET /rbl/list

Get a list of RBL's to check against.
[
    "access.redhawk.org",
    "b.barracudacentral.org",
    "bl.shlink.org",
    "bl.spamcannibal.org",
    "bl.spamcop.net"
]

## GET /rbl/:ip

Check :ip against all RBL's at /rbl/list. If the check takes longer than 60 seconds an error will be thrown.
:ip - An IPv4 address to check
[
    {
        access.redhawk.org: {
            blacklisted: false,
            result: [ ]
        }
    },
    {
        b.barracudacentral.org: {
            blacklisted: true,
            result: [
                "127.0.0.2"
            ]
        }
    },
    {
        bl.shlink.org: {
            blacklisted: false,
            result: [ ]
        }
    }
]

## GET /rbl/:ip/:rbl_list

Check :ip against a specific set of :rbl_list servers. If the check takes longer than 60 seconds an error will be thrown.
:rbl_list - A + separated list of hosts, eg: b.barracudacentral.org+bl.shlink.org
[
    {
        b.barracudacentral.org: {
        blacklisted: true,
        result: [
            "127.0.0.2"
        ]
        }
    },
    {
        bl.shlink.org: {
        blacklisted: false,
        result: [

        ]
        }
    }
]

# License
MIT License. 
