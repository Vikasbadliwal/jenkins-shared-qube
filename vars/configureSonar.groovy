def call(String bastionIp) {
    echo "Extracting AWS RDS Endpoint and establishing secure tunnel..."
    
    // Extract the exact RDS URL dynamically from Terraform's outputs
    def rdsEndpoint = sh(script: 'terraform output -raw rds_endpoint', returnStdout: true).trim()
    
    def sshArgs = "-o StrictHostKeyChecking=no -o ProxyCommand=\"ssh -W %h:%p -q ubuntu@${bastionIp} -i sonarkey.pem -o StrictHostKeyChecking=no\""
    
    // Inject the endpoint and credentials directly into Ansible
    sh """
        ansible-playbook -i ansible/aws_ec2.yml ansible/site.yml \
        --private-key sonarkey.pem \
        -u ubuntu \
        -e "db_endpoint=${rdsEndpoint}" \
        -e "sonarqube_db_user=sonar" \
        -e "sonarqube_db_password=YourSecureDBPassword123" \
        --ssh-common-args='${sshArgs}'
    """
}
