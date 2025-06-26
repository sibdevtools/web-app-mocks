import React, { useState } from 'react';
import { Accordion, Button, Table } from 'react-bootstrap';
import SyntaxHighlighter from 'react-syntax-highlighter';
import { docco } from 'react-syntax-highlighter/dist/esm/styles/hljs';
import 'react-syntax-highlighter/dist/esm/languages/hljs/javascript';
import 'react-syntax-highlighter/dist/esm/languages/hljs/python';
import { Task01Icon, TaskDone01Icon } from 'hugeicons-react';
import './CodeDocumentation.css';

type ExampleCode = {
  description: string,
} & (
  {
    implementation: string
  }
  |
  {
    implementations: Record<'javascript' | 'python', string>
  }
  )

interface ExampleSection {
  name: string,
  examples?: ExampleCode[],
  sections?: ExampleSection[]
}


const rqExamples = [
  {
    description: 'Get request object',
    implementation: `wam.request()`,
  },
  {
    description: 'Get request remote host',
    implementations: {
      javascript: `const rq = wam.request();
const remoteHost = rq.remoteHost;`,
      python: `rq = wam.request()
remoteHost = rq.remoteHost`,
    }
  },
  {
    description: 'Get request remote address',
    implementations: {
      javascript: `const rq = wam.request();
const remoteAddress = rq.remoteAddress;`,
      python: `rq = wam.request()
remoteAddress = rq.remoteAddress`,
    }
  },
  {
    description: 'Get request method',
    implementations: {
      javascript: `const rq = wam.request();
const method = rq.method;`,
      python: `rq = wam.request()
method = rq.method`,
    }
  },
  {
    description: 'Get request path',
    implementations: {
      javascript: `const rq = wam.request();
const path = rq.path;`,
      python: `rq = wam.request()
path = rq.path`,
    }
  },
];
const rqHeadersExamples = [
  {
    description: 'Get request headers as map',
    implementations: {
      javascript: `const rq = wam.request();
const headers = rq.headers;`,
      python: `rq = wam.request()
headers = rq.headers`,
    }
  },
  {
    description: 'Get request header values string list',
    implementations: {
      javascript: `const rq = wam.request();
const values = rq.header('Content-Type');`,
      python: `rq = wam.request()
values = rq.header("Content-Type")`,
    }
  },
  {
    description: 'Get request header first string value',
    implementations: {
      javascript: `const rq = wam.request();
const value = rq.headerFirst('Content-Type');`,
      python: `rq = wam.request()
value = rq.headerFirst("Content-Type")`,
    }
  },
];
const rqQueryParamsExamples = [
  {
    description: 'Get request query parameter values string list',
    implementations: {
      javascript: `const rq = wam.request();
const values = rq.queryParam('someId');`,
      python: `rq = wam.request()
values = rq.queryParam("someId")`,
    }
  },
  {
    description: 'Get request query parameter first string value',
    implementations: {
      javascript: `const rq = wam.request();
const value = rq.queryParamFirst('someId');`,
      python: `rq = wam.request()
value = rq.queryParamFirst("someId")`,
    }
  },
];
const rqCookiesExamples = [
  {
    description: 'Get request cookie',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")`,
    }
  },
  {
    description: 'Cookie domain',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
cookie.getDomain();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.getDomain()`,
    }
  },
  {
    description: 'Cookie max age',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
cookie.getMaxAge();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.getMaxAge()`,
    }
  },
  {
    description: 'Cookie path',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
cookie.getPath();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.getPath()`,
    }
  },
  {
    description: 'Cookie secure flag',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
cookie.getSecure();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.getSecure()`,
    }
  },
  {
    description: 'Cookie name',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
cookie.getName();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.getName()`,
    }
  },
  {
    description: 'Cookie value',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
cookie.getValue();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.getValue()`,
    }
  },
  {
    description: 'Cookie is HTTP only flag',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
cookie.isHttpOnly();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.isHttpOnly()`,
    }
  },
  {
    description: 'Cookie get attribute value',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
const value = cookie.getAttribute("attributeKey");`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
value = cookie.getAttribute("attributeKey")`,
    }
  },
  {
    description: 'Cookie get attributes map',
    implementations: {
      javascript: `const rq = wam.request();
const cookie = rq.cookie('cookie1');
const attributes = cookie.getAttributes();`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
attributes = cookie.getAttributes()`,
    }
  },
];
const rqBodyExamples = [
  {
    description: 'Get request body as byte array',
    implementations: {
      javascript: `const rq = wam.request();
const body = rq.bytes();`,
      python: `rq = wam.request()
body = rq.bytes()`,
    }
  },
  {
    description: 'Get request body as string',
    implementations: {
      javascript: `const rq = wam.request();
const body = rq.text();`,
      python: `rq = wam.request()
body = rq.text()`,
    }
  },
  {
    description: 'Get request body as JSON',
    implementations: {
      javascript: `const rq = wam.request();
const body = rq.json();`,
      python: `rq = wam.request()
body = rq.json()`,
    }
  },
];

const rsExamples = [
  {
    description: 'Get response object',
    implementation: `wam.response()`,
  },
  {
    description: 'Set response status',
    implementations: {
      javascript: `const rs = wam.response();
rs.status(503);`,
      python: `rs = wam.response()
rs.status(503)`
    }
  },
  {
    description: 'Set response header',
    implementations: {
      javascript: `const rs = wam.response();
rs.header('key-string', 'value-string');`,
      python: `rs = wam.response()
rs.header("key-string", "value-string")`
    }
  },
];
const rsCookieExamples = [
  {
    description: 'Add cookie to response. Insecure and HTTP only.',
    implementations: {
      javascript: `const rs = wam.response();
rs.cookie('name', 'value');`,
      python: `rs = wam.response()
rs.cookie("name", "value")`
    }
  },
  {
    description: 'Add cookie to response',
    implementations: {
      javascript: `const rs = wam.response();
const httpOnly = false;
const secure = true;
rs.cookie('name', 'value', secure, httpOnly);`,
      python: `rs = wam.response()
httpOnly = False
secure = True
rs.cookie("name", "value", secure, httpOnly)`
    }
  },
];
const rsBodyExamples = [
  {
    description: 'Write to response text in UTF-8',
    implementations: {
      javascript: `const rs = wam.response();
rs.plain('any-string');`,
      python: `rs = wam.response()
rs.plain("any-string")`
    }
  },
  {
    description: 'Write to response text in UTF-8 and set status and content type header',
    implementations: {
      javascript: `const rs = wam.response();
rs.plain(500, 'text/plain', 'any-string');`,
      python: `rs = wam.response()
rs.plain(500, "text/plain", "any-string")`
    }
  },
  {
    description: 'Write to response bytes array',
    implementations: {
      javascript: `const rs = wam.response();
rs.bytes([48, 49]);`,
      python: `rs = wam.response()
rs.bytes([48, 49])`
    }
  },
  {
    description: 'Write to bytes array and set status and content type header',
    implementations: {
      javascript: `const rs = wam.response();
rs.bytes(500, 'text/plain', [48, 49]);`,
      python: `rs = wam.response()
rs.bytes(500, "text/plain", [48, 49])`
    }
  },
  {
    description: 'Write to response JSON',
    implementations: {
      javascript: `const rs = wam.response();
rs.json({key: 'value'});`,
      python: `rs = wam.response()
rs.json({"key": "value"})`,
    }
  },
  {
    description: 'Write to JSON and set status and content type header',
    implementations: {
      javascript: `const rs = wam.response();
rs.json(500, 'text/plain', {key: 'value'});`,
      python: `rs = wam.response()
rs.json(500, "text/plain", {"key": "value"})`
    }
  },
];

const sessionsExamples = [
  {
    description: 'Get sessions service',
    implementation: `wam.sessions()`,
  },
  {
    description: 'Get session by ID with last version',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")`
    }
  },
  {
    description: 'Get session by ID and specific version',
    implementations: {
      javascript: `const sessions = wam.sessions();
const version = 1;
const session = sessions.get('session-id', version);`,
      python: `sessions = wam.sessions()
version = 1
session = sessions.get("session-id", version)`
    }
  },
  {
    description: 'Get session by ID and specific version (version is string)',
    implementations: {
      javascript: `const sessions = wam.sessions();
const version = '1';
const session = sessions.get('session-id', version);`,
      python: `sessions = wam.sessions()
version = "1"
session = sessions.get("session-id", version)`
    }
  },
  {
    description: 'Create session',
    implementations: {
      javascript: `const sessions = wam.sessions();
const sections = {
  sectionKey: {
    sectionAttributeKey: 'sectionAttributeValue',
    sectionAttributeKey2: true,
    sectionAttributeKey3: 42,
    sectionAttributeKey4: ['item1', 'item2'],
    sectionAttributeKey5: {key: 'value'},
  }
};
const ownerTypeCode = 'SERVICE'; // or 'USER'
const ownerId = '123456';
const permissions = ['PERMISSION1', 'PERMISSION2'];
const session = sessions.create(sections, ownerTypeCode, ownerId, permissions);`,
      python: `sessions = wam.sessions()
sections = {
  "sectionKey": {
    "sectionAttributeKey": "sectionAttributeValue",
    "sectionAttributeKey2": True,
    "sectionAttributeKey3": 42,
    "sectionAttributeKey4": ["item1", "item2"],
    "sectionAttributeKey5": {"key": "value"},
  }
}
ownerTypeCode = "SERVICE" # or "USER"
ownerId = "123456"
permissions = ["PERMISSION1", "PERMISSION2"]
session = sessions.create(sections, ownerTypeCode, ownerId, permissions)`,
    }
  },
];
const sessionIdExamples = [
  {
    description: 'Get session id',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
const sessionId = session.getId();`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
sessionId = session.getId()`
    }
  },
  {
    description: 'Get session uid',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
const sessionId = session.getId();
const sessionUID = sessionId.getUID();`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
sessionId = session.getId()
sessionUID = sessionId.getUID()`
    }
  },
  {
    description: 'Get session version',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
const sessionId = session.getId();
const sessionVersion = sessionId.getVersion();`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
sessionId = session.getId()
sessionVersion = sessionId.getVersion()`
    }
  }
];
const sessionAttributesExamples = [
  {
    description: 'Get attribute value',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
const attributeValue = session.get('sectionKey', 'attributeKey');`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
attributeValue = session.get("sectionKey", "attributeKey")`
    }
  },
  {
    description: 'Get attribute names',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
const attributesNames = session.getAttributeNames('sectionKey');`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
attributesNames = session.getAttributeNames("sectionKey")`
    }
  },
  {
    description: 'Create attribute, fail if attribute already exists',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
session.add('sectionKey', 'attributeKey1', 'attributeValue');`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
session.add("sectionKey", "attributeKey1", "attributeValue")`
    }
  },
  {
    description: 'Set attribute, create if attribute does not exist',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
session.set('sectionKey', 'attributeKey1', 'attributeValue');`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
session.set("sectionKey", "attributeKey1", "attributeValue")`
    }
  },
  {
    description: 'Remove attribute',
    implementations: {
      javascript: `const sessions = wam.sessions();
const session = sessions.get('session-id');
session.remove('sectionKey', 'attributeKey1');`,
      python: `sessions = wam.sessions()
session = sessions.get("session-id")
session.remove("sectionKey", "attributeKey1")`
    }
  },
];

const kafkaExamples = [
  {
    description: 'Get Kafka service',
    implementations: {
      javascript: `const kafka = wam.kafka();`,
      python: `kafka = wam.kafka()`
    }
  },
];

const kafkaPublishingExamples = [
  {
    description: 'Publish message',
    implementations: {
      javascript: `const publishRq = {
    bootstrapServers: ["localhost:9092"],
    topic: "topic-to-publish",
    maxTimeout: 30000,
    partition: 0, // optional
    timestamp: 0, // optional
    key: [48, 49], // bytes array, optional
    value: [48, 49], // bytes array, optional
    headers: {
      headerKey: [48, 49] // bytes array, optional
    }
};
const publishRs = kafka.publish(publishRq);`,
      python: `publishRq = {
    "bootstrapServers": ["localhost:9092"],
    "topic": "topic-to-publish",
    "maxTimeout": 30000,
    "partition": 0, # optional
    "timestamp": 0, # optional
    "key": [48, 49], # bytes array, optional
    "value": [48, 49], # bytes array, optional
    "headers": {
      "headerKey": [48, 49] # bytes array, optional
    }
}
publishRs = kafka.publish(publishRq)`
    }
  },
  {
    description: 'Publish message into existed bootstrap group',
    implementations: {
      javascript: `const publishRq = {
    groupCode: "kafkaGroupCode",
    topic: "topic-to-publish",
    partition: 0, // optional
    timestamp: 0, // optional
    key: [48, 49], // bytes array, optional
    value: [48, 49], // bytes array, optional
    headers: {
      headerKey: [48, 49] // bytes array, optional
    },
    maxTimeout: 30000 // optional
};
const publishRs = kafka.publish(publishRq);`,
      python: `publishRq = {
    "groupCode": "kafkaGroupCode",
    "topic": "topic-to-publish",
    "partition": 0, # optional
    "timestamp": 0, # optional
    "key": [48, 49], # bytes array, optional
    "value": [48, 49], # bytes array, optional
    "headers": {
      "headerKey": [48, 49] # bytes array, optional
    },
    "maxTimeout": 30000 # optional
}
publishRs = kafka.publish(publishRq)`
    }
  },
  {
    description: 'Publish template message into group',
    implementations: {
      javascript: `const publishRq = {
    groupCode: "kafkaGroupCode",
    topic: "topic-to-publish",
    templateCode: "template-code",
    partition: 0, // optional
    timestamp: 0, // optional
    key: [48, 49], // bytes array, optional
    input: { // input depends on template
      param1: "value",
      param2: 123
    },
    headers: {
      headerKey: [48, 49] // bytes array, optional
    },
    maxTimeout: 30000 // optional
};
const publishRs = kafka.publishTemplate(publishRq);`,
      python: `publishRq = {
    "groupCode": "kafkaGroupCode",
    "topic": "topic-to-publish",
    templateCode: "template-code",
    "partition": 0, # optional
    "timestamp": 0, # optional
    "key": [48, 49], # bytes array, optional
    "input": { # input depends on template
      "param1": "value",
      "param2": 123
    },
    "headers": {
      "headerKey": [48, 49] # bytes array, optional
    },
    "maxTimeout": 30000 # optional
}
publishRs = kafka.publishTemplate(publishRq)`
    }
  },
];

const kafkaConsumingExamples = [
  {
    description: 'Consume at most 5 messages from the beginning',
    implementations: {
      javascript: `const consumeRq = {
    groupCode: "kafkaGroupCode",
    topic: "topic-to-publish",
    maxMessages: 5,
    maxTimeout: 30000,
};
const consumeRs = kafka.getMessages(consumeRq);`,
      python: `consumeRq = {
    "groupCode": "kafkaGroupCode",
    "topic": "topic-to-publish",
    "maxMessages": 5,
    "maxTimeout": 30000,
}
consumeRs = kafka.getMessages(consumeRq)`
    }
  },
  {
    description: 'Consume at most 5 messages from group from the end',
    implementations: {
      javascript: `const consumeRq = {
    groupCode: "kafkaGroupCode",
    topic: "topic-to-publish",
    maxMessages: 5,
    maxTimeout: 30000,
    direction: "LATEST",
};
const consumeRs = kafka.getMessages(consumeRq);`,
      python: `consumeRq = {
    "groupCode": "kafkaGroupCode",
    "topic": "topic-to-publish",
    "maxMessages": 5,
    "maxTimeout": 30000,
    "direction": "LATEST",
}
consumeRs = kafka.getMessages(consumeRq)`
    }
  },
  {
    description: 'Consume messages with fixed bootstrap servers',
    implementations: {
      javascript: `const consumeRq = {
    bootstrapServers: ["localhost:9092"],
    topic: "topic-to-publish",
    maxMessages: 5,
    maxTimeout: 30000,
};
const consumeRs = kafka.getMessages(consumeRq);`,
      python: `consumeRq = {
    "bootstrapServers": ["localhost:9092"],
    "topic": "topic-to-publish",
    "maxMessages": 5,
    "maxTimeout": 30000,
}
consumeRs = kafka.getMessages(consumeRq)`
    }
  },
];

const kafkaConsumingResponseExamples = [
  {
    description: 'Topic name',
    implementations: {
      javascript: `const topic = consumeRs.topic();`,
      python: `topic = consumeRs.topic()`
    }
  },
  {
    description: 'Messages list',
    implementations: {
      javascript: `const messages = consumeRs.messages();`,
      python: `messages = consumeRs.messages()`
    }
  },
];

const kafkaConsumedMessageResponseExamples = [
  {
    description: 'Partition',
    implementations: {
      javascript: `const partition = messages[0].partition();`,
      python: `partition = messages[0].partition()`
    }
  },
  {
    description: 'Offset',
    implementations: {
      javascript: `const offset = messages[0].offset();`,
      python: `offset = messages[0].offset()`
    }
  },
  {
    description: 'Timestamp',
    implementations: {
      javascript: `const timestamp = messages[0].timestamp();`,
      python: `timestamp = messages[0].timestamp()`
    }
  },
  {
    description: 'Timestamp Type',
    implementations: {
      javascript: `const timestampType = messages[0].timestampType();`,
      python: `timestampType = messages[0].timestampType()`
    }
  },
];

const kafkaConsumedMessageHeadersResponseExamples = [
  {
    description: 'Headers dictionary',
    implementations: {
      javascript: `const headers = messages[0].headers();`,
      python: `headers = messages[0].headers()`
    }
  },
  {
    description: 'Header binary value',
    implementations: {
      javascript: `const header = messages[0].header("header-key");`,
      python: `header = messages[0].header("header-key")`
    }
  },
  {
    description: 'Header text value',
    implementations: {
      javascript: `const header = messages[0].headerText("header-key");`,
      python: `header = messages[0].headerText("header-key")`
    }
  },
  {
    description: 'Header JSON value',
    implementations: {
      javascript: `const header = messages[0].headerJson("header-key");`,
      python: `header = messages[0].headerJson("header-key")`
    }
  },
];

const kafkaConsumedMessageKeyResponseExamples = [
  {
    description: 'Serialized Key Size',
    implementations: {
      javascript: `const serializedKeySize = messages[0].serializedKeySize();`,
      python: `serializedKeySize = messages[0].serializedKeySize()`
    }
  },
  {
    description: 'Message binary key',
    implementations: {
      javascript: `const key = messages[0].key();`,
      python: `key = messages[0].key()`
    }
  },
  {
    description: 'Message text key',
    implementations: {
      javascript: `const key = messages[0].textKey();`,
      python: `key = messages[0].textKey()`
    }
  },
  {
    description: 'Message JSON key',
    implementations: {
      javascript: `const key = messages[0].jsonKey();`,
      python: `key = messages[0].jsonKey()`
    }
  },
];

const kafkaConsumedMessageValueResponseExamples = [
  {
    description: 'Serialized Value Size',
    implementations: {
      javascript: `const serializedValueSize = messages[0].serializedValueSize();`,
      python: `serializedValueSize = messages[0].serializedValueSize()`
    }
  },
  {
    description: 'Message binary value',
    implementations: {
      javascript: `const value = messages[0].value(); // [72, 105, 33]`,
      python: `value = messages[0].value() # [72, 105, 33]`
    }
  },
  {
    description: 'Message text value',
    implementations: {
      javascript: `const value = messages[0].textValue(); // 'Hi!'`,
      python: `value = messages[0].textValue() # "Hi!"`
    }
  },
  {
    description: 'Message JSON value',
    implementations: {
      javascript: `const value = messages[0].jsonValue(); // '"Hi!"'`,
      python: `value = messages[0].jsonValue() # '"Hi!"'`
    }
  },
];

const kafkaPublishingResponseExamples = [
  {
    description: 'Message offset',
    implementations: {
      javascript: `const offset = publishRs.offset();`,
      python: `offset = publishRs.offset()`,
    }
  },
  {
    description: 'Message timestamp',
    implementations: {
      javascript: `const timestamp = publishRs.timestamp();`,
      python: `timestamp = publishRs.timestamp()`,
    }
  },
  {
    description: 'Message serialized key size',
    implementations: {
      javascript: `const serializedKeySize = publishRs.serializedKeySize();`,
      python: `serializedKeySize = publishRs.serializedKeySize()`,
    }
  },
  {
    description: 'Message serialized value size',
    implementations: {
      javascript: `const serializedValueSize = publishRs.serializedValueSize();`,
      python: `serializedValueSize = publishRs.serializedValueSize()`,
    }
  },
  {
    description: 'Message partition',
    implementations: {
      javascript: `const partition = publishRs.partition();`,
      python: `partition = publishRs.partition()`,
    }
  },
];

const keyValueStorageExamples = [
  {
    description: 'Get key-value storage service',
    implementations: {
      javascript: `const kvs = wam.keyValueStorage();`,
      python: `kvs = wam.keyValueStorage()`
    }
  },
];

const keyValueStorageRecordExamples = [
  {
    description: 'Get value holder',
    implementations: {
      javascript: `const valueHolder = kvs.get("space-name", "key");`,
      python: `valueHolder = kvs.get("space-name", "key")`
    }
  },
  {
    description: 'Get value from holder',
    implementations: {
      javascript: `const value = valueHolder.value();`,
      python: `value = valueHolder.value()`
    }
  },
  {
    description: 'Get meta information from holder',
    implementations: {
      javascript: `const meta = valueHolder.meta();`,
      python: `meta = valueHolder.meta()`
    }
  },
  {
    description: 'Set record state',
    implementations: {
      javascript: `const epochMilliseconds = 1750832700000;
const meta = kvs.set({
  space: "space-name",
  key: "key",
  value: "some-value", // any serializable value
  expiredAt: epochMilliseconds // optional
});`,
      python: `epochMilliseconds = 1750832700000
meta = kvs.set({
  "space": "space-name",
  "key": "key",
  "value": "some-value", # any serializable value
  "expiredAt": epochMilliseconds # optional
});`
    }
  },
  {
    description: 'Prolongate record',
    implementations: {
      javascript: `const epochMilliseconds = 1750832700000;
const meta = kvs.prolongate("space-name", "key", epochMilliseconds);`,
      python: `epochMilliseconds = 1750832700000
meta = kvs.prolongate("space-name", "key", epochMilliseconds)`
    }
  },
  {
    description: 'Remove value from space by key',
    implementations: {
      javascript: `kvs.delete("space-name", "key");`,
      python: `kvs.delete("space-name", "key")`
    }
  },
];

const keyValueStorageRecordMetaExamples = [
  {
    description: 'Get creation epoch milliseconds from meta information',
    implementations: {
      javascript: `const createdAt = meta.createdAt();`,
      python: `createdAt = meta.createdAt()`
    }
  },
  {
    description: 'Get modification epoch milliseconds from meta information',
    implementations: {
      javascript: `const modifiedAt = meta.modifiedAt();`,
      python: `modifiedAt = meta.modifiedAt()`
    }
  },
  {
    description: 'Get expiration epoch milliseconds from meta information',
    implementations: {
      javascript: `const expiredAt = meta.expiredAt();`,
      python: `expiredAt = meta.expiredAt()`
    }
  },
  {
    description: 'Get version from meta information',
    implementations: {
      javascript: `const version = meta.version();`,
      python: `version = meta.version()`
    }
  },
];

const keyValueStorageSpaceExamples = [
  {
    description: 'Get set of all spaces names',
    implementations: {
      javascript: `const spaces = kvs.getSpaces();`,
      python: `spaces = kvs.getSpaces()`
    }
  },
  {
    description: 'Get set of all keys in space',
    implementations: {
      javascript: `const keys = kvs.getKeys("space-name");`,
      python: `keys = kvs.getKeys("space-name")`
    }
  },
  {
    description: 'Delete entire space',
    implementations: {
      javascript: `kvs.delete("space-name");`,
      python: `kvs.delete("space-name")`
    }
  },
];

const allExamples: ExampleSection = {
  name: 'Examples',
  sections: [
    {
      name: 'Request',
      examples: rqExamples,
      sections: [
        {
          name: 'Headers',
          examples: rqHeadersExamples
        },
        {
          name: 'Query Parameters',
          examples: rqQueryParamsExamples
        },
        {
          name: 'Cookies',
          examples: rqCookiesExamples
        },
        {
          name: 'Body',
          examples: rqBodyExamples
        }
      ]
    },
    {
      name: 'Response',
      examples: rsExamples,
      sections: [
        {
          name: 'Cookie',
          examples: rsCookieExamples
        },
        {
          name: 'Body',
          examples: rsBodyExamples
        }
      ]
    },
    {
      name: 'Sessions',
      examples: sessionsExamples,
      sections: [
        {
          name: 'Session Actions',
          sections: [
            {
              name: 'Session ID',
              examples: sessionIdExamples
            },
            {
              name: 'Session attributes',
              examples: sessionAttributesExamples
            }
          ]
        }
      ]
    },
    {
      name: 'Kafka',
      examples: kafkaExamples,
      sections: [
        {
          name: 'Publishing',
          examples: kafkaPublishingExamples,
          sections: [
            {
              name: 'Publish result',
              examples: kafkaPublishingResponseExamples
            }
          ]
        },
        {
          name: 'Consuming',
          examples: kafkaConsumingExamples,
          sections: [
            {
              name: 'Consuming result',
              examples: kafkaConsumingResponseExamples,
              sections: [
                {
                  name: 'Message',
                  examples: kafkaConsumedMessageResponseExamples,
                  sections: [
                    {
                      name: 'Headers',
                      examples: kafkaConsumedMessageHeadersResponseExamples
                    },
                    {
                      name: 'Key',
                      examples: kafkaConsumedMessageKeyResponseExamples
                    },
                    {
                      name: 'Value',
                      examples: kafkaConsumedMessageValueResponseExamples
                    }
                  ]
                },
              ]
            }
          ]
        }
      ]
    },
    {
      name: 'Key-value storage',
      examples: keyValueStorageExamples,
      sections: [
        {
          name: 'Space operations',
          examples: keyValueStorageSpaceExamples
        },
        {
          name: 'Record operations',
          examples: keyValueStorageRecordExamples,
          sections: [
            {
              name: 'Meta information',
              examples: keyValueStorageRecordMetaExamples
            }
          ]
        }
      ]
    },
    {
      name: "Utils",
      examples: [
        {
          description: "Get utils",
          implementations: {
            javascript: 'const utils = wam.utils()',
            python: 'utils = wam.utils()'
          }
        }
      ],
      sections: [
        {
          name: 'Binary utils',
          examples: [
            {
              description: "Get binary utils",
              implementations: {
                javascript: 'const binary = utils.binary()',
                python: 'binary = utils.binary()'
              }
            },
            {
              description: "Convert string, number to binary array",
              implementations: {
                javascript: 'const array = binary.toBytes("test")',
                python: 'array = binary.toBytes("test")'
              }
            },
            {
              description: "Convert binary array to string",
              implementations: {
                javascript: 'const array = binary.toString([1, 2, 3])',
                python: 'array = binary.toString([1, 2, 3])'
              }
            },
            {
              description: "Convert binary array to int",
              implementations: {
                javascript: 'const array = binary.toInt([1, 2, 3, 4])',
                python: 'array = binary.toInt([1, 2, 3, 4])'
              }
            },
            {
              description: "Convert binary array to long",
              implementations: {
                javascript: 'const array = binary.toLong([1, 2, 3, 4, 5, 6, 7, 8])',
                python: 'array = binary.toLong([1, 2, 3, 4, 5, 6, 7, 8])'
              }
            },
            {
              description: "Convert binary array to float",
              implementations: {
                javascript: 'const array = binary.toFloat([1, 2, 3, 4])',
                python: 'array = binary.toFloat([1, 2, 3, 4])'
              }
            },
            {
              description: "Convert binary array to double",
              implementations: {
                javascript: 'const array = binary.toDouble([1, 2, 3, 4, 5, 6, 7, 8])',
                python: 'array = binary.toDouble([1, 2, 3, 4, 5, 6, 7, 8])'
              }
            }
          ]
        },
        {
          name: 'Base64 utils',
          examples: [
            {
              description: "Get Base64 utils",
              implementations: {
                javascript: 'const base64 = utils.base64()',
                python: 'base64 = utils.base64()'
              }
            },
            {
              description: "Encode array to array",
              implementations: {
                javascript: 'const array = base64.encode([1, 2, 3, 4])',
                python: 'array = base64.encode([1, 2, 3, 4])'
              }
            },
            {
              description: "Encode array to string",
              implementations: {
                javascript: 'const string = base64.encodeToString([1, 2, 3, 4])',
                python: 'string = base64.encodeToString([1, 2, 3, 4])'
              }
            },
            {
              description: "Decode array or string to array",
              implementations: {
                javascript: 'const array = base64.decode([1, 2, 3, 4])',
                python: 'array = base64.decode([1, 2, 3, 4])'
              }
            },
          ]
        },
        {
          name: 'JSON utils',
          examples: [
            {
              description: "Get JSON utils",
              implementations: {
                javascript: 'const json = utils.json()',
                python: 'json = utils.json()'
              }
            },
            {
              description: "Serialize object to binary array with json",
              implementations: {
                javascript: 'const array = json.serialize({key: "value"})',
                python: 'array = json.serialize({"key": "value"})'
              }
            },
            {
              description: "Dump object to string json",
              implementations: {
                javascript: 'const dumped = json.dump({key: "value"})',
                python: 'dumped = json.dump({"key": "value"})'
              }
            },
            {
              description: "Deserialize object from binary array with json",
              implementations: {
                javascript: 'const deserialized = json.deserialize([1, 2, 3, 4])',
                python: 'deserialized = json.deserialize([1, 2, 3, 4])'
              }
            },
            {
              description: "Parse object from json string",
              implementations: {
                javascript: 'const parsed = json.parse("{\"key\":42}")',
                python: 'parsed = json.parse("{\"key\":42}")'
              }
            },
          ]
        }
      ]
    }
  ]
};

const CodeDocumentation: React.FC<{ mode: 'javascript' | 'python', section?: ExampleSection }> = ({
                                                                                                    mode,
                                                                                                    section = allExamples
                                                                                                  }) => {
  const [copied, setCopied] = useState<Array<any>>([]);

  return (
    <Accordion className={'mb-2'}>
      <Accordion.Item eventKey={`examples-${section.name}`}>
        <Accordion.Header>{section.name}</Accordion.Header>
        <Accordion.Body>
          {section.examples && (
            <Table>
              <thead>
              <tr>
                <th>Description</th>
                <th>Code</th>
              </tr>
              </thead>
              <tbody>
              {
                section.examples.map(it =>
                  (
                    <tr>
                      <td>{it.description}</td>
                      <td>
                        {'implementation' in it && (
                          <div style={{ position: 'relative', borderRadius: '8px', overflow: 'hidden' }}>
                            <Button
                              variant={'link'}
                              className={'text-secondary code-clipboard-btn'}
                              size={'sm'}
                              onClick={async () => {
                                const copiedNew = [...copied, it];
                                setCopied(copiedNew);
                                await navigator.clipboard.writeText(it.implementation);
                                setTimeout(() => setCopied(copied.filter(e => e !== it)), 1500);
                              }}
                            >
                              {copied.some(e => e === it) ? (<TaskDone01Icon size={16} />) : (<Task01Icon size={16} />)}
                            </Button>
                            <SyntaxHighlighter language={mode} style={docco}>
                              {it.implementation}
                            </SyntaxHighlighter>
                          </div>
                        )}
                        {'implementations' in it && (
                          <div style={{ position: 'relative', borderRadius: '8px', overflow: 'hidden' }}>
                            <Button
                              variant={'link'}
                              className={'text-secondary code-clipboard-btn'}
                              size={'sm'}
                              onClick={async () => {
                                const copiedNew = [...copied, it];
                                setCopied(copiedNew);
                                await navigator.clipboard.writeText(it.implementations[mode]);
                                setTimeout(() => setCopied(copied.filter(e => e !== it)), 1500);
                              }}
                            >
                              {copied.some(e => e === it) ? (<TaskDone01Icon size={16} />) : (<Task01Icon size={16} />)}
                            </Button>
                            <SyntaxHighlighter language={mode} style={docco}>
                              {it.implementations[mode]}
                            </SyntaxHighlighter>
                          </div>
                        )}
                      </td>
                    </tr>
                  )
                )
              }
              </tbody>
            </Table>
          )}
          {section.sections?.map(it => (
            <CodeDocumentation mode={mode} section={it} />
          ))}
        </Accordion.Body>
      </Accordion.Item>
    </Accordion>
  );
};

export default CodeDocumentation;
