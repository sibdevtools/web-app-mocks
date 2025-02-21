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
const cookie = rq.cookie('cookie1');
cookie.getDomain(); // cookie domain
cookie.getMaxAge(); // cookie max age
cookie.getPath(); // cookie path
cookie.getSecure(); // cookie secure flag
cookie.getName(); // cookie name
cookie.getValue(); // cookie value
cookie.isHttpOnly(); // http only cookie flag
cookie.getAttribute("attributeKey"); // get attribute value
cookie.getAttributes(); // get attributes map`,
      python: `rq = wam.request()
cookie = rq.cookie("cookie1")
cookie.getDomain() # cookie domain
cookie.getMaxAge() # cookie max age
cookie.getPath() # cookie path
cookie.getSecure() # cookie secure flag
cookie.getName() # cookie name
cookie.getValue() # cookie value
cookie.isHttpOnly() # http only cookie flag
cookie.getAttribute("attributeKey") # get attribute value
cookie.getAttributes() # get attributes map`,
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
