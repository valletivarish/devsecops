# 1) Public SG ingress (HIGH/CRITICAL)
resource "aws_security_group" "ssh_open" {
  name        = "ssh-open"
  description = "Open SSH to the world"
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress { from_port = 0 to_port = 0 protocol = "-1" cidr_blocks = ["0.0.0.0/0"] }
}

# 2) Public S3 bucket ACL + no block public access (CRITICAL)
resource "aws_s3_bucket" "public_bucket" { bucket = "demo-public-bucket-xyz" }
resource "aws_s3_bucket_acl" "public_acl" {
  bucket = aws_s3_bucket.public_bucket.id
  acl    = "public-read"
}
# no aws_s3_bucket_public_access_block -> triggers multiple checks

# 3) Wildcard IAM policy (CRITICAL)
resource "aws_iam_policy" "bad_policy" {
  name   = "BadPolicy"
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{ Effect = "Allow", Action = "*", Resource = "*" }]
  })
}

# 4) Unencrypted EBS (MEDIUM/HIGH depending on check)
resource "aws_ebs_volume" "unencrypted" {
  availability_zone = "us-east-1a"
  size              = 8
  encrypted         = false
}

# 5) Publicly accessible RDS (HIGH)
resource "aws_db_instance" "public_rds" {
  identifier              = "public-rds"
  engine                  = "mysql"
  instance_class          = "db.t3.micro"
  username                = "root"
  password                = "insecurepassword"
  allocated_storage       = 20
  publicly_accessible     = true
  skip_final_snapshot     = true
}
# 6) Security group allows all traffic (CRITICAL)
resource "aws_security_group" "all_open" {
  name        = "allow-all"
  description = "Allows all inbound traffic"
  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# 7) S3 bucket without encryption (HIGH)
resource "aws_s3_bucket" "unencrypted_bucket" {
  bucket = "unencrypted-bucket-xyz"
}
# Missing aws_s3_bucket_server_side_encryption_configuration

# 8) EC2 instance with plaintext secrets (HIGH)
resource "aws_instance" "bad_instance" {
  ami           = "ami-12345678"
  instance_type = "t2.micro"
  user_data     = <<-EOF
    #!/bin/bash
    echo "DB_PASSWORD=SuperSecret123" >> /etc/environment
  EOF
}

# 9) CloudTrail without log validation (HIGH)
resource "aws_cloudtrail" "insecure_trail" {
  name                          = "insecure-trail"
  s3_bucket_name                = aws_s3_bucket.public_bucket.id
  include_global_service_events = true
  enable_log_file_validation    = false # Bad practice
}

# 10) EKS cluster public endpoint (HIGH)
resource "aws_eks_cluster" "public_eks" {
  name     = "public-eks"
  role_arn = "arn:aws:iam::123456789012:role/eks-role"
  vpc_config {
    endpoint_public_access = true
    endpoint_private_access = false
  }
}