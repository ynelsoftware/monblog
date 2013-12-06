function () 
{
    if (!this.tags) 
    {
    	return;
    }

    for (index in this.tags) 
    {
        emit(this.tags[index], 1);
    }
}