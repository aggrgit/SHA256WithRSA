import base64
import os
from cryptography.hazmat.primitives import hashes
from cryptography.hazmat.primitives.asymmetric import rsa, padding
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.backends import default_backend


def load_private_key(filename):
    """Load private key from PEM or DER format."""
    with open(filename, 'rb') as f:
        key_data = f.read()

    if filename.endswith(".pem"):
        return serialization.load_pem_private_key(key_data, password=None, backend=default_backend())
    else:
        return serialization.load_der_private_key(key_data, password=None, backend=default_backend())


def load_public_key(filename):
    """Load public key from PEM or DER format."""
    with open(filename, 'rb') as f:
        key_data = f.read()

    if filename.endswith(".pem"):
        return serialization.load_pem_public_key(key_data, backend=default_backend())
    else:
        return serialization.load_der_public_key(key_data, backend=default_backend())


def generate_signature(string_to_sign, private_key):
    """Generate the signature for a string using the private key."""
    signature = private_key.sign(
        string_to_sign.encode('utf-8'),
        padding.PKCS1v15(),
        hashes.SHA256()
    )
    return base64.b64encode(signature).decode('utf-8')


def validate_signature(signature, string_to_validate, public_key):
    """Validate the signature against the string using the public key."""
    signature_bytes = base64.b64decode(signature)
    try:
        public_key.verify(
            signature_bytes,
            string_to_validate.encode('utf-8'),
            padding.PKCS1v15(),
            hashes.SHA256()
        )
        return True
    except Exception as e:
        print(f"Validation failed: {e}")
        return False


def main():
    string_to_sign = '{"test":"test"}'
    private_key_file = 'C:/Users/Admin/Documents/SHA256WithRSA/src/resources/private_key.pem'  # Change to your private key file
    public_key_file = 'C:/Users/Admin/Documents/SHA256WithRSA/src/resources/public_key.der'  # Change to your public key file

    # Load private and public keys
    private_key = load_private_key(private_key_file)
    public_key = load_public_key(public_key_file)

    # Generate signature
    signature = generate_signature(string_to_sign, private_key)
    print(f"String_to_sign: {string_to_sign}")
    print(f"Signature: {signature}")

    # Validate the signature
    validation_success = validate_signature(signature, string_to_sign, public_key)
    print(f"Validate success: {validation_success}")


if __name__ == "__main__":
    main()
