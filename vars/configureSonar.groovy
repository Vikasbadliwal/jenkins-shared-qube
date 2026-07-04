// vars/configureSonar.groovy
def call(String bastionIp) {
    echo "Establishing secure tunnel to Bastion at ${bastionIp}..."

    // Build the dynamic SSH proxy command
    def sshArgs = "-o ProxyCommand=\"ssh -W %h:%p -q ubuntu@${bastionIp} -i sonarkey.pem -o StrictHostKeyChecking=no\""

    // Execute the Ansible playbook
    sh """
        ansible-playbook -i ansible/aws_ec2.yml ansible/deploy-sonarqube.yml \
        --ssh-common-args='${sshArgs}'
    """
}
