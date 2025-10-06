# Insecure Terraform example
provider "aws" {
  region = "us-east-1"
}

resource "aws_s3_bucket" "bad_bucket" {
  bucket = "my-insecure-bucket"

  # ❌ No encryption enabled
  acl    = "public-read" # ❌ Public bucket
}
