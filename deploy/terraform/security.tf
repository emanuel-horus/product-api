resource "aws_security_group" "product_sg" {
  name        = "productapi_sg"
  description = "productapi security group"
  vpc_id      = aws_vpc.product_vpc.id
}

resource "aws_security_group_rule" "sgr_pub_out" {
  from_port         = 0
  protocol          = "-1"
  security_group_id = aws_security_group.product_sg.id
  to_port           = 0
  type              = "egress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "srg_ssh_in" {
  from_port         = 22
  protocol          = "tcp"
  security_group_id = aws_security_group.product_sg.id
  to_port           = 22
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_security_group_rule" "srg_http_in" {
  from_port         = 80
  protocol          = "tcp"
  security_group_id = aws_security_group.product_sg.id
  to_port           = 80
  type              = "ingress"
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_key_pair" "product_key" {
  key_name   = "productapi_key"
  public_key = file("~/.ssh/productapi_key.pub")
}