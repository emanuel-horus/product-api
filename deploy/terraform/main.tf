resource "aws_vpc" "product_vpc" {
  cidr_block = "10.0.0.0/16"
  enable_dns_hostnames = true
  enable_dns_support = true

  tags = {
    "Name" = "productapi_vpc"
  }
}

resource "aws_subnet" "product_subnet" {
  vpc_id     = aws_vpc.product_vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "us-east-1a"
  map_public_ip_on_launch = true

  tags = {
    Name = "productapi_subnet_pub"
  }
}

resource "aws_internet_gateway" "product_igw" {
  vpc_id = aws_vpc.product_vpc.id

  tags = {
    "Name" = "productapi_igw"
  }
}

resource "aws_route_table" "product_rtb" {
  vpc_id = aws_vpc.product_vpc.id

  tags = {
    "Name" = "productapi_rtb"
  }
}

resource "aws_route" "product_route" {
  route_table_id = aws_route_table.product_rtb.id
  destination_cidr_block = "0.0.0.0/0"
  gateway_id = aws_internet_gateway.product_igw.id
}

resource "aws_route_table_association" "product_rtb_ass" {
  route_table_id = aws_route_table.product_rtb.id
  subnet_id = aws_subnet.product_subnet.id
}

resource "aws_instance" "product_ec2" {
  instance_type = "t2.micro"
  key_name = aws_key_pair.product_key.id
  vpc_security_group_ids = [aws_security_group.product_sg.id]
  subnet_id = aws_subnet.product_subnet.id

  ami = data.aws_ami.product_ami.id

  user_data = file("userdata.tpl")

  root_block_device {
    volume_size = 8
  }

  tags = {
    Name = "productapi_ec2"
  }
}