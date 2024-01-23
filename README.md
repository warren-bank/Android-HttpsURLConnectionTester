### [HttpsURLConnection Tester](https://github.com/warren-bank/Android-HttpsURLConnectionTester)

A little Android utility app for the purpose of testing `HttpsURLConnection` to help debug SSL/TLS connection errors.

#### Helpful Links

* _Protocols_ and _Cipher Suites_ supported by Web Hosts:
  - [letsencrypt.org](https://www.ssllabs.com/ssltest/analyze.html?d=letsencrypt.org)

* _Protocols_ and _Cipher Suites_ supported by HTTP clients:
  - [Android 4.4.2](https://www.ssllabs.com/ssltest/viewClient.html?name=Android&version=4.4.2)

* Hosted Web Sites for testing that support a single _Protocol_:
  - [badssl.com](https://github.com/chromium/badssl.com)
    * [TLSv1.0](https://tls-v1-0.badssl.com:1010/)
    * [TLSv1.1](https://tls-v1-1.badssl.com:1011/)
    * [TLSv1.2](https://tls-v1-2.badssl.com:1012/)

#### Android 4.x

Interesting problem:

* the list of cipher suites reported by _SSL Labs_ for Android HTTP clients is [based on tests of its `WebView`](https://community.qualys.com/thread/16297)
* [excerpt](https://github.com/AntennaPod/AntennaPod/issues/2814#issuecomment-426367766):
  * Further research leads me to believe that the discrepancy is due to the fact that Android 4.4 (API 19) actually does _not_ support any of these (TLS 1.2) cipher suites natively.
  * `WebView` has started to gain more modern TLS support than the underlying Android operating system itself.

My own observations:

* [releases](https://github.com/warren-bank/Android-HttpsURLConnectionTester/releases) v2.x work pretty well
  - the only problem that I've encountered.. and it's non-trivial.. is that hosts using a certificate signed by the most common _Let's Encrypt_ root certificate almost always require unsupported cipher suites
    * _SSL Labs_ reports that some are supported
    * `WebView` can successfully load the URL
    * `HttpsURLConnection` fails
* [my post on the _Let's Encrypt_ forum](https://community.letsencrypt.org/t/help-which-certificates-to-install-on-android-4-4/212077) asks for advice, and includes a more detailed discussion of:
  - how I updated all system root certificates
  - lists of hosts with a successful TLS handshake
  - lists of hosts with a failed TLS handshake

Workaround using _Conscrypt_:

My understanding of [_Conscrypt_](https://github.com/google/conscrypt/) is that it:

* implements _many_ modern cipher suites
* plugs directly into the Java Security model as the preferred Provider
* isn't tiny
  - about 4 MB for all 4x ABIs
  - about 1 MB per ABI
* can be either be:
  - bundled with each individual app as an internal library
  - installed once in a [standalone app](https://f-droid.org/packages/com.mendhak.conscryptprovider/), and then shared with any other app that includes a [small amount of boilerplate code](https://github.com/mendhak/Conscrypt-Provider#instructions-for-developers)

Release Flavors:

* `withInternalConscryptSecurityProvider`
  - _Conscrypt_ is bundled as an internal library
  - releases a separate APK for each ABI
* `withSharedExternalConscryptOrDefaultSecurityProvider`
  - _Conscrypt_ is loaded from a [shared app](https://f-droid.org/packages/com.mendhak.conscryptprovider/)
  - falls back to use the default native Security Provider when either:
    * this app isn't available
    * this app isn't signed by a trusted source

#### Legal

* copyright: [Warren Bank](https://github.com/warren-bank)
* license: [GPL-2.0](https://www.gnu.org/licenses/old-licenses/gpl-2.0.txt)
