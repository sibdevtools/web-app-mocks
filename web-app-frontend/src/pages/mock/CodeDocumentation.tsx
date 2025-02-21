import React from 'react';
import { Accordion, Table } from 'react-bootstrap';
import SyntaxHighlighter from 'react-syntax-highlighter';
import { docco } from 'react-syntax-highlighter/dist/esm/styles/hljs';
import 'react-syntax-highlighter/dist/esm/languages/hljs/javascript';
import 'react-syntax-highlighter/dist/esm/languages/hljs/python';


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
    }
  ]
};

const CodeDocumentation: React.FC<{ mode: 'javascript' | 'python', section?: ExampleSection }> = ({
                                                                                                    mode,
                                                                                                    section = allExamples
                                                                                                  }) => {
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
                          <SyntaxHighlighter language={mode} style={docco}>
                            {it.implementation}
                          </SyntaxHighlighter>
                        )}
                        {'implementations' in it && (
                          <SyntaxHighlighter language={mode} style={docco}>
                            {it.implementations[mode]}
                          </SyntaxHighlighter>
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
