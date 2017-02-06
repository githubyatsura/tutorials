## Simple integration TEST (WEBDAV API of Apache Jackrabbit 2.14) with help of TestNG 6.x + maven plugins (failsafe + exec.)

Examples contains:

 * an example of a simple integration test devoted to WEBDAV API of [Apache Jackrabbit 2.14](http://jackrabbit.apache.org/jcr/downloads.html#v2.14).
 * a sample of [Apache HttpComponents](https://hc.apache.org/) use.
 
### How to build

Please, use [Apache Maven](https://maven.apache.org/) to build the project.

```bash
$ git clone https://github.com/githubyatsura/tutorials.git
$ cd maven-copy-execute-dependency-example
$ mvn clean package
```

### How to run

The project uses [Apache Maven 'failsafe' plugin](http://maven.apache.org/surefire/maven-failsafe-plugin/), [MojoHaus Maven 'exec' plugin](http://www.mojohaus.org/exec-maven-plugin/) to automatically install and run example.

```bash
$ mvn integation-test
```

### License

```text
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to <http://unlicense.org>