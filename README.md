Siddhi IO SQS
======================================

[![Jenkins Build Status](https://wso2.org/jenkins/job/siddhi/job/siddhi-io-sqs/badge/icon)](https://wso2.org/jenkins/job/siddhi/job/siddhi-io-sqs/)
[![GitHub Release](https://img.shields.io/github/release/siddhi-io/siddhi-io-sqs.svg)](https://github.com/siddhi-io/siddhi-io-sqs/releases)
[![GitHub Release Date](https://img.shields.io/github/release-date/siddhi-io/siddhi-io-sqs.svg)](https://github.com/siddhi-io/siddhi-io-sqs/releases)
[![GitHub Open Issues](https://img.shields.io/github/issues-raw/siddhi-io/siddhi-io-sqs.svg)](https://github.com/siddhi-io/siddhi-io-sqs/issues)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/siddhi-io/siddhi-io-sqs.svg)](https://github.com/siddhi-io/siddhi-io-sqs/commits/master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

The **siddhi-io-sqs extension** is an extension to <a target="_blank" href="https://wso2.github.io/siddhi">Siddhi</a> 
that used to receive and publish events via AWS SQS Service. This extension allows users to subscribe to a SQS queue 
and receive/publish SQS messages.

Find some useful links below:

* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-io-sqs">Source code</a>
* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-io-sqs/releases">Releases</a>
* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-io-sqs/issues">Issue tracker</a>

## Downloads
* Versions 3.x and above with group id `io.siddhi.extension.*` from <a target="_blank" href="https://mvnrepository.com/artifact/io.siddhi.extension.io.sqs/siddhi-io-sqs/">here</a>.
* Versions 2.x and lower with group id `org.wso2.extension.siddhi.` from  <a target="_blank" href="https://mvnrepository.com/artifact/org.wso2.extension.siddhi.io.sqs/siddhi-io-sqs">here</a>.

## Latest API Docs 

Latest API Docs is <a target="_blank" href="https://wso2-extensions.github.io/siddhi-io-sqs/api/2.0.0">2.0.0</a>.

# Features

* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-io-sqs/api/2.0.0/#sqs-sink">sqs</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#sink">(Sink)</a>*<br><div style="padding-left: 1em;"><p>The sqs sink pushes the events into a sqs broker using the AMQP protocol</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-io-sqs/api/2.0.0/#sqs-source">sqs</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#source">(Source)</a>*<br><div style="padding-left: 1em;"><p>The sqs source receives the events from the sqs broker via the AMQP protocol. </p></div>

## Dependencies
There are no other dependencies needed for this extension.

## Installation
For installing this extension on various siddhi execution environments refer Siddhi documentation section on <a target="_blank" href="https://siddhi.io/redirect/add-extensions.html">adding extensions</a>.

# Support and Contribution

* We encourage users to ask questions and get support via <a target="_blank" href="https://stackoverflow.com/questions/tagged/siddhi">StackOverflow</a>, make sure to add the `siddhi` tag to the issue for better response.

* If you find any issues related to the extension please report them on <a target="_blank" href="https://github.com/siddhi-io/siddhi-execution-string/issues">the issue tracker</a>.

* For production support and other contribution related information refer <a target="_blank" href="https://siddhi.io/community/">Siddhi Community</a> documentation.
