// vars/provisionInfra.groovy
def call() {
    echo " Initializing Terraform..."
    sh 'terraform init'

    echo " Provisioning AWS Multi-Tier Infrastructure..."
    sh 'terraform apply -auto-approve'

    // Dynamically extract the outputs
    def bastionIp = sh(script: 'terraform output -raw bastion_public_ip', returnStdout: true).trim()
    def albUrl = sh(script: 'terraform output -raw alb_dns_name', returnStdout: true).trim()

    // Return them as a map so the Jenkinsfile can use them
    return [bastionIp: bastionIp, albUrl: albUrl]
}
