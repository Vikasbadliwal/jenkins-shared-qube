def call(String bastionIp) {
    echo "Extracting AWS RDS Endpoint and preparing SSH Keys..."
    
    // 1. Prepare the SSH key securely in the background
    sh 'cp $SSH_KEY_PATH sonarkey.pem'
    sh 'chmod 400 sonarkey.pem'

    def rdsEndpoint = sh(script: 'terraform output -raw rds_endpoint', returnStdout: true).trim()
    def sshArgs = "-o StrictHostKeyChecking=no -o ProxyCommand=\"ssh -W %h:%p -q ubuntu@${bastionIp} -i sonarkey.pem -o StrictHostKeyChecking=no\""
    
    sh """
        ansible-playbook -i ansible/aws_ec2.yml ansible/site.yml \
        --private-key sonarkey.pem \
        -u ubuntu \
        -e "db_endpoint=${rdsEndpoint}" \
        -e "sonarqube_db_user=sonar" \
        -e "sonarqube_db_password=\$TF_VAR_db_password" \
        --ssh-common-args='${sshArgs}'
    """
}
