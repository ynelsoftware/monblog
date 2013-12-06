function (previous, current) 
{
    var count = 0;

    for (index in current) 
    {
        count += current[index];
    }

    return count;
}