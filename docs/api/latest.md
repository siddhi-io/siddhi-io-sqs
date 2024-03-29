# API Docs - v3.0.1

!!! Info "Tested Siddhi Core version: *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/">5.1.21</a>*"
    It could also support other Siddhi Core minor versions.

## Sink

### sqs *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#sink">(Sink)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">SQS sink allows users to connect and publish messages to an AWS SQS Queue. It has the ability to only publish Text messages</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
@sink(type="sqs", queue="<STRING>", access.key="<STRING>", secret.key="<STRING>", region="<STRING>", message.group.id="<STRING>", deduplication.id="<STRING>", delay.interval="<INT>", @map(...)))
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">queue</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Queue url which SQS Sink should connect to</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">access.key</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Access Key for the Amazon Web Services. (This is a mandatory field and should be provided either in the deployment.yml or in the sink definition itself)</p></td>
        <td style="vertical-align: top">none</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">secret.key</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Secret Key of the Amazon User. (This is a mandatory field and should be provided either in the deployment.yml or in the sink definition itself)</p></td>
        <td style="vertical-align: top">none</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">region</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Amazon Web Service Region</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">message.group.id</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">ID of the group that the message belong to(only applicable for FIFO Queues)</p></td>
        <td style="vertical-align: top">null</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">deduplication.id</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">ID by which a FIFO queue identifies the duplication in the queue(only applicable for FIFO queues)</p></td>
        <td style="vertical-align: top">null</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">delay.interval</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Time in seconds for how long the message remain in the queue until it is available for the consumers to consume.</p></td>
        <td style="vertical-align: top">-1</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
@sink(type='sqs',queue='https://amazon.sqs.queue.url',access.key='aws.access.key',secret.key='aws.secret.key',region='us-east-1',delay.interval='5',message.group.id='group-1',@map(type='xml') )define stream outStream(symbol string, deduplicationID string);
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Above example demonstrate how an SQS sink is getting configured in order to publish messages to a SQS queue.<br>Once an event is received by outStream, an xml message will be generated by 'xml' mapper from the attribute values of the event. Then SQS sink will connect to the queue using provided configurations and send the message to the queue.<br></p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
@sink(type='sqs',queue='https://amazon.sqs.queue.fifo',access.key='aws.access.key',secret.key='aws.secret.key',region='us-east-1',delay.interval='5',deduplication.id='{{deduplicationID}}',message.group.id='group-1',@map(type='xml') )define stream outStream(symbol string, deduplicationID string);
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Above example demonstrate how an SQS sink is getting configured in order to publish messages to a SQS FIFO queue.<br>Once an event is received by outStream, an xml message will be generated by 'xml' mapper from the attribute values of the event. SQS sink will connect to the queue using provided configurations and send the messages to the queue.<br>For each message deduplciation id will be selected from the attriibute 'deduplicationID' in the outStream.<br></p>
<p></p>
## Source

### sqs *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#source">(Source)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">SQS source allows users to connect and consume messages from a AWS SQS Queue. It has the ability to receive Text messages</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
@source(type="sqs", queue="<STRING>", access.key="<STRING>", secret.key="<STRING>", region="<STRING>", polling.interval="<INT>", wait.time="<INT>", max.number.of.messages="<INT>", visibility.timeout="<INT>", delete.messages="<BOOL>", delete.retry.interval="<INT>", max.number.of.delete.retry.attempts="<INT>", number.of.parallel.consumers="<INT>", @map(...)))
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">queue</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Queue name which SQS Source should subscribe to</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">access.key</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Access Key for the Amazon Web Services. (This is a mandatory field and should be provided either in the deployment.yml or in the source definition itself)</p></td>
        <td style="vertical-align: top">null</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">secret.key</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Secret Key of the Amazon User. (This is a mandatory field and should be provided either in the deployment.yml or in the source definition itself)</p></td>
        <td style="vertical-align: top">null</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">region</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Amazon Web Service Region</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">polling.interval</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Interval (in milliseconds) between two message retrieval operations</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">wait.time</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Maximum amount (in seconds) that a polling call will wait for a message to become available in the queue</p></td>
        <td style="vertical-align: top">-1</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">max.number.of.messages</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Maximum number of messages retrieved from the queue per polling call (Actual maybe smaller than this even if there's more messages in the queue)</p></td>
        <td style="vertical-align: top">1</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">visibility.timeout</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The length of time (in seconds) for which a message received from a queue will be invisible to other consumers(only applicable if consumer doesn't purge the received messages from the queue).</p></td>
        <td style="vertical-align: top">-1</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">delete.messages</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Should the message be deleted from the queue after consuming it.</p></td>
        <td style="vertical-align: top">delete.messages</td>
        <td style="vertical-align: top">BOOL</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">delete.retry.interval</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Time interval (in milliseconds) consumer should retry to delete a message in the case of failure during a message delete operation.</p></td>
        <td style="vertical-align: top">5000</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">max.number.of.delete.retry.attempts</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Maximum number retry attempts to be performed in case of a failure.</p></td>
        <td style="vertical-align: top">10</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">number.of.parallel.consumers</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Size of the thread pool that should be used for polling.</p></td>
        <td style="vertical-align: top">1</td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
@source(type='sqs',queue='http://aws.sqs.queue.url',access.key='aws.access.key',secret.key='aws.secret.key',region='us-east-2',polling.interval='5000',max.number.of.messages='10',number.of.parallel.consumers='1',purge.messages='true',wait.time='2',visibility.timeout='30',delete.retry.interval='1000',max.number.of.delete.retry.attempts='10',@map(type='xml',enclosing.element="//events",@attributes(symbol='symbol', message_id='trp:MESSAGE_ID') ))define stream inStream (symbol string, message_id string);
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Above example demonstrate how an SQS source is getting configured in order to consume messages from an SQS queue.<br>SQS source will establish the connection to a queue using given configurations and start consuming xml messages from the queue.<br>Once a message is received by the source from the given queue, 'xml' mapper will generate a siddhi event from that message and pass it to the inStream.</p>
<p></p>
