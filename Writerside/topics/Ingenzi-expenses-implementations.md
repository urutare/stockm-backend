# Ingenzi expenses implementations

### Creating transactions

- type(Expense, income, transfer)
- cash Account
- category (Account type based on type): if type is income, we use account types under income account type, for expense,
  we use account types under expense account type.
- account (Optional): Account type will have a boolean field to indicate if it is required accounts. which will be
  accounts has its account type code.
- description

### Creating category:

While creating a category, we will create account type with corresponding account.
Account to be created will have a property called `referencingAccountTypeCode` which will be the code of the account
type.
. this will allow us to create a transaction with the account type.

By creating account with account type, we will automatically use the corresponding account or create a new account if
not found.

### Creating a customer.

To create a customer, we should allow it only if the user has selected account type of `Cash sale` or ` Credit sale`.
While creating a customer, we will do this actions.

- Create a customerEntity
- Create account under cash sale with reference to customer id
- Create account under credit sale with reference to customer id
- Create account under customer receivable with reference to customer id

### Account type:

Account type will have this new fields:

- shouldSelectAccount - boolean: This will indicate that, user has to select account while creating a transaction.
- accountsLabel - string: this label will be shown to user as a label for accounts while creating a transactin.
- optinalAccounts - boolean: this will enforce user to select account while creating a transaction.
  -originBusinessId - string: this will be used to identify the business id of business created the account type.
- unverified (Optional): if true, will be visible only to the origin business
