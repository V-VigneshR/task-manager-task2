# Complete AWS EKS Deployment Guide for Task Manager API

## Prerequisites
- AWS CLI installed and configured
- kubectl installed
- Docker installed
- AWS account with appropriate permissions

## Step 1: Create EKS Cluster

### Option A: Using AWS Console (Easier)
1. Go to AWS EKS Console
2. Click "Create cluster"
3. **Cluster name**: `task-api-cluster`
4. **Kubernetes version**: 1.33
5. **Service role**: Create new or use existing EKS service role
6. **VPC**: Use default or create new
7. **Subnets**: Select at least 2 subnets in different AZs
8. Click "Create" 

### Option B: Using AWS CLI
```bash
# Create cluster
aws eks create-cluster \
  --name task-manager-cluster \
  --version 1.28 \
  --role-arn arn:aws:iam::YOUR_ACCOUNT:role/eksServiceRole \
  --resources-vpc-config subnetIds=subnet-xxx,subnet-yyy

# Wait for cluster to be active
aws eks wait cluster-active --name task-manager-cluster
```

## Step 2: Create Node Group

### In AWS Console:
1. Go to your cluster → **Compute** tab
2. Click "Add Node Group"
3. **Name**: `task-manager-nodes`
4. **Instance type**: `t3.medium` (or t3.small for cost saving)
5. **Desired size**: 2
6. **Min size**: 1
7. **Max size**: 2
8. Click "Create"

### Using CLI:
```bash
aws eks create-nodegroup \
  --cluster-name task-manager-cluster \
  --nodegroup-name task-manager-nodes \
  --instance-types t3.medium \
  --node-role arn:aws:iam::YOUR_ACCOUNT:role/NodeInstanceRole \
  --subnets subnet-xxx subnet-yyy \
  --desired-size 2 \
  --min-size 1 \
  --max-size 4
```

## Step 3: Configure kubectl

```bash
# Update kubeconfig
aws eks update-kubeconfig --region YOUR_REGION --name task-manager-cluster

# Verify connection
kubectl get nodes
```

## Step 4: Create ECR Repository

```bash
# Create repository
aws ecr create-repository \
  --repository-name task-manager-api \
  --region YOUR_REGION

# Get login token
aws ecr get-login-password --region YOUR_REGION | docker login --username AWS --password-stdin YOUR_ACCOUNT.dkr.ecr.YOUR_REGION.amazonaws.com
```

## Step 5: Test Your Deployment

```bash
# Get LoadBalancer URL
export LOADBALANCER_URL=$(kubectl get svc task-manager-service -o jsonpath='{.status.loadBalancer.ingress[0].hostname}')
echo "API URL: http://$LOADBALANCER_URL"

# Test API
curl http://$LOADBALANCER_URL/api/v1/tasks/health

# Create a task
curl -X PUT http://$LOADBALANCER_URL/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "eks-test-1",
    "name": "EKS Test Task",
    "owner": "AWS User",
    "command": "echo Hello from EKS!"
  }'

# Execute task
curl -X PUT http://$LOADBALANCER_URL/api/v1/tasks/eks-test-1/execute
```

## Step 9: Verify Persistent Storage

```bash
# Delete MongoDB pod
kubectl delete pod -l app=mongodb

# Wait for new pod
kubectl get pods -w

# Verify data persists
curl http://$LOADBALANCER_URL/api/v1/tasks/eks-test-1
```


CLEANUP: 
# Delete Kubernetes resources
kubectl delete -f app.yaml
kubectl delete -f mongodb.yaml

# Delete EKS cluster
aws eks delete-cluster --name task-manager-cluster
```

## NOTE:
Persistent Storage for Mongodb causes as issue. The status remains pending for a long time. Due to that, host based storage is being used due to time constraints.

n- ✅ **Persistent storage**: EBS volume survives pod deletions
- ✅ **CI/CD**: CodePipeline + CodeBuild for automated deployments
