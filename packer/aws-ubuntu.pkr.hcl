variable "aws_region" {
  type    = string
  default = "us-east-1"
}

variable "source_ami" {
  type    = string
  default = "ami-08c40ec9ead489470" # Ubuntu 22.04 LTS
}

variable "env_key_id" {
  type    = string
  default = "018880469153"
}

variable "ssh_username" {
  type    = string
  default = "ubuntu"
}

variable "subnet_id" {
  type    = string
   default = "subnet-0c3202697f19d8f93"
}

# https://www.packer.io/plugins/builders/amazon/ebs
source "amazon-ebs" "my-ami" {
  region     = "${var.aws_region}"
  ami_name        = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  ami_description = "AMI for CSYE 6225"
  ami_users     = ["${var.env_key_id}"]


  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }


  instance_type = "t2.micro"
  source_ami    = "${var.source_ami}"
  ssh_username  = "${var.ssh_username}"
  #subnet_id     = "${var.subnet_id}"

  launch_block_device_mappings {
    delete_on_termination = true
    device_name           = "/dev/sda1"
    volume_size           = 8
    volume_type           = "gp2"
  }
}

build {
  sources = ["source.amazon-ebs.my-ami"]

  provisioner "file" {
    source="../target/webapp-0.0.1-SNAPSHOT.jar"
    destination="~/"
    
  }
  provisioner "file" {
    destination = "~/"
    source      = "../app.service"
  }

  provisioner "file" {
    source="../scripts/script.sh"
    destination="~/"

  }
  provisioner "file" {
    destination = "~/"
    source      = "../packer/cloudwatch_config.json"
  }

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    inline = [
      "sudo apt-get update --fix-missing",
      "sudo apt install -f",
      "sudo apt-get upgrade -y",
      "sudo apt install openjdk-17-jdk openjdk-17-jre -y",
      "sudo curl -o /root/amazon-cloudwatch-agent.deb https://s3.amazonaws.com/amazoncloudwatch-agent/debian/amd64/latest/amazon-cloudwatch-agent.deb",
      "sudo dpkg -i -E /root/amazon-cloudwatch-agent.deb",
      "sudo mkdir logs",
    ]
  }
  post-processor "manifest" {
    output = "manifest.json"
    strip_path = true
    custom_data = {

      my_custom_data = "example"

    }

  }

}
