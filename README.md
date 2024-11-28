# Application for encode SHA256WithRSA
This application is used to demo how to encrypt SHA256WithRSA using java or python.


## Requirements
Before using this project, ensure that you have the following installed:
### JAVA
- **Java Development Kit (JDK)**: Version 8 or higher. You can download it from [AdoptOpenJDK](https://adoptopenjdk.net/) or [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- **OpenSSL** (Optional, if you need to generate RSA keys).

### PYTHON
- **Python IDEA (Installer)**: Version 3 or higher. You can download it from [Python Installer](https://www.python.org/downloads/windows/).
- **Pip (Installer)**: py -m ensurepip --upgrade. You need set the pip in "Environment Variables"


## Generate private and public key
Before start the application please generate the private and public key.

Step 1: Download the OpenSSL installer and install it or may refer ([Guidance to install openssl](https://medium.com/thesecmaster/step-by-step-procedure-to-install-openssl-on-the-windows-platform-37e7ccee682d).)

Step 2: Generate Private Key or Public Key
## Useful OpenSSL comands:

Generate a 2048-bit RSA private key

> `$ openssl genrsa -out private_key.pem 2048`

Generate a public key

> `$ openssl rsa -in private_key.pem -pubout > public_key.pem`

Convert private Key to PKCS#8 format

> `$ openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem -out private_key.der -nocrypt`

Output public key portion in DER format

> `$ openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der`

## Run application to test


### JAVA
Step 1: Find the private and public key and move to [Folder](./src/resources)

Step 2: Run [Cryptograpic application](./src/Cryptographic.java) to test and validate the string to sign.

### Summary of Key Changes:

- The instructions for **running the program** now include the specific command `java Cryptographic` to run the `main()` method from the `Cryptographic` class.
- This updated version makes it clear that the user needs to compile the Java file first and then execute it using the `java` command to run the `main()` method.

With these updates, users will now be able to compile and run your Java program using the `main()` method directly.

### Python
> py python.py

### Remark
#### JAVA
Please replace your private key or public key
> `generateSignature(string_to_sign, "${change to your private key file}");`
> `validateSignature(signature, string_to_sign, "${change to your public key file}");`

#### Python
> `private_key_file = ${change to your private key file}`
> `public_key_file = ${Change to your public key file}`










