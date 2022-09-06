# Smartym Test task

### Task

See the details of the task [here](http://test.devenv.smartym.by/).

### What was done

The implemented workflow differs from the workflow described in the task.
Current workflow has the following steps:
 - Show the `payment app page` with a link to a `payment page`;
 - When user pressing `New payment` link, application starting authorization process against `Smartym bank simulation`;
 - After a successful authorization user is redirected to the `payment page` with an input for Creditor IBAN, Creditor Name, amount, currency, description and `submit` button;
 - When user pressing the `submit` button, then application sends a payment request using `accessToken`;
