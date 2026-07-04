def call(String bastionIp) {
    echo "Establishing secure tunnel to Bastion at ${bastionIp}..."
    
    // Notice StrictHostKeyChecking=no is now at the very front AND inside the ProxyCommand
    def sshArgs = "-o StrictHostKeyChecking=no -o ProxyCommand=\"ssh -W %h:%p -q ubuntu@${bastionIp} -i sonarkey.pem -o StrictHostKeyChecking=no\""
    
    sh """
        ansible-playbook -i ansible/aws_ec2.yml ansible/site.yml \
        --ssh-common-args='${sshArgs}'
    """
}
