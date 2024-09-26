export const contextPath = '/web/app/mocks/ui/'

export const methods = [
  'GET',
  'OPTIONS',
  'POST',
  'PUT',
  'PATCH',
  'DELETE'
] as const;

export type Method = typeof methods[number];

export const statusCodes = new Map([
    [100, 'Continue'] as const,
    [101, 'Switching Protocols'] as const,
    [102, 'Processing'] as const,
    [103, 'Early Hints'] as const,
    [200, 'OK'] as const,
    [201, 'Created'] as const,
    [202, 'Accepted'] as const,
    [203, 'Non-Authoritative Information'] as const,
    [204, 'No Content'] as const,
    [205, 'Reset Content'] as const,
    [206, 'Partial Content'] as const,
    [300, 'Multiple Choices'] as const,
    [301, 'Moved Permanently'] as const,
    [302, 'Found'] as const,
    [303, 'See Other'] as const,
    [304, 'Not Modified'] as const,
    [305, 'Use Proxy'] as const,
    [306, 'Unused'] as const,
    [307, 'Temporary Redirect'] as const,
    [308, 'Permanent Redirect'] as const,
    [400, 'Bad Request'] as const,
    [401, 'Unauthorized'] as const,
    [402, 'Payment Required'] as const,
    [403, 'Forbidden'] as const,
    [404, 'Not Found'] as const,
    [405, 'Method Not Allowed'] as const,
    [406, 'Not Acceptable'] as const,
    [407, 'Proxy Authentication Required'] as const,
    [408, 'Request Timeout'] as const,
    [409, 'Conflict'] as const,
    [410, 'Gone'] as const,
    [411, 'Length Required'] as const,
    [412, 'Precondition Failed'] as const,
    [413, 'Request Entity Too Large'] as const,
    [414, 'Request-URI Too Long'] as const,
    [415, 'Unsupported Media Type'] as const,
    [416, 'Requested Range Not Satisfiable'] as const,
    [417, 'Expectation Failed'] as const,
    [418, 'I\'m a teapot'] as const,
    [421, 'Misdirected Request'] as const,
    [422, 'Unprocessable Entity'] as const,
    [423, 'Locked'] as const,
    [425, 'Too Early'] as const,
    [426, 'Upgrade Required'] as const,
    [428, 'Precondition Required'] as const,
    [429, 'Too Many Requests'] as const,
    [431, 'Request Header Fields Too Large'] as const,
    [451, 'Unavailable For Legal Reasons'] as const,
    [500, 'Internal Server Error'] as const,
    [501, 'Not Implemented'] as const,
    [502, 'Bad Gateway'] as const,
    [503, 'Service Unavailable'] as const,
    [504, 'Gateway Timeout'] as const,
    [505, 'HTTP Version Not Supported'] as const,
    [506, 'Variant Also Negotiates'] as const,
    [507, 'Insufficient Storage'] as const,
    [511, 'Network Authentication Required'] as const,
    [520, 'Web server is returning an unknown error'] as const,
    [522, 'Connection timed out'] as const,
    [524, 'A timeout occurred'] as const,
  ]
)

export type MapKey<T extends Map<any, any>> = T extends Map<infer K, any> ? K : never

export type StatusCode = MapKey<typeof statusCodes>

export interface ContentTypeValue {
  caption: string,
  aceType: string
}

export const contentTypes = new Map<string, ContentTypeValue>([
    ['application/json', { caption: 'JSON', aceType: 'json' }] as const,
    ['application/javascript', { caption: 'JavaScript', aceType: 'javascript' }] as const,
    ['application/xml', { caption: 'XML', aceType: 'xml' }] as const,
    ['text/html', { caption: 'HTML', aceType: 'html' }] as const,
    ['text/css', { caption: 'CSS', aceType: 'css' }] as const,
    ['text/csv', { caption: 'CSV', aceType: 'plain_text' }] as const,
    ['text/plain', { caption: 'Text plain', aceType: 'plain_text' }] as const,
    ['text/markdown', { caption: 'Markdown', aceType: 'markdown' }] as const,
  ]
)

export type ContentType = MapKey<typeof contentTypes>

export const mockTypes = [
  'STATIC',
  'JS',
  'PYTHON',
] as const;

export type MockType = typeof mockTypes[number];
