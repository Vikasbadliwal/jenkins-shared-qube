def call(String bastionIp) {
    echo "Establishing secure tunnel to Bastion at ${bastionIp}..."
    
    def sshArgs = "-o StrictHostKeyChecking=no -o ProxyCommand=\"ssh -W %h:%p -q ubuntu@${bastionIp} -i sonarkey.pem -o StrictHostKeyChecking=no\""
    
    // Added --private-key and -u ubuntu so Ansible can authenticate with the private server
    sh """
        ansible-playbook -i ansible/aws_ec2.yml ansible/site.yml \
        --private-key sonarkey.pem \
        -u ubuntu \
        --ssh-common-args='${sshArgs}'
    """
}
