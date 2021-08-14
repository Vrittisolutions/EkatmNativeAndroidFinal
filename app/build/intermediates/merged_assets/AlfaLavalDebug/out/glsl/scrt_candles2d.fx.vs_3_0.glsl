uniform highp vec4 screenSize;
uniform highp mat4 matWorldViewProj;
attribute highp vec4 vTexCoord0;
attribute highp vec4 vColor;
attribute highp vec4 vColor1;
attribute highp vec2 vTexCoord1;
attribute highp vec4 vTexCoord2;
attribute highp vec4 vTexCoord4;
varying highp vec4 xlv_COLOR0;
varying highp vec2 xlv_TEXCOORD0;
varying highp vec4 xlv_TEXCOORD1;
void main ()
{
  highp vec4 tmpvar_1;
  tmpvar_1 = vec4(0.0, 0.0, 0.0, 0.0);
  highp vec2 tmpvar_2;
  tmpvar_2 = vec2(0.0, 0.0);
  highp vec4 tmpvar_3;
  if ((vTexCoord2.x == -1.0)) {
    tmpvar_3 = (vTexCoord0 * matWorldViewProj);
  } else {
    highp vec4 outPosition_4;
    highp vec4 tmpvar_5;
    tmpvar_5.zw = vec2(0.0, 1.0);
    tmpvar_5.xy = vTexCoord0.xy;
    highp vec4 tmpvar_6;
    tmpvar_6 = (tmpvar_5 * matWorldViewProj);
    outPosition_4 = (tmpvar_6 / abs(tmpvar_6.w));
    highp vec4 tmpvar_7;
    tmpvar_7.zw = vec2(0.0, 1.0);
    tmpvar_7.xy = vTexCoord0.zw;
    highp vec4 tmpvar_8;
    tmpvar_8 = (tmpvar_7 * matWorldViewProj);
    highp vec2 tmpvar_9;
    tmpvar_9 = normalize(((tmpvar_8 / 
      abs(tmpvar_8.w)
    ).xy - outPosition_4.xy));
    outPosition_4.x = (outPosition_4.x - ((vTexCoord1.x * screenSize.z) * tmpvar_9.y));
    outPosition_4.y = (outPosition_4.y + ((vTexCoord1.x * screenSize.w) * tmpvar_9.x));
    tmpvar_3 = outPosition_4;
  };
  highp float tmpvar_10;
  if ((vTexCoord2.x >= 0.0)) {
    tmpvar_10 = 0.0;
  } else {
    tmpvar_10 = 1.0;
  };
  tmpvar_2 = vTexCoord4.xy;
  highp vec4 tmpvar_11;
  tmpvar_11.xy = vTexCoord1;
  tmpvar_11.zw = vTexCoord2.xy;
  tmpvar_1.xzw = tmpvar_3.xzw;
  tmpvar_1.y = -(tmpvar_3.y);
  gl_Position = tmpvar_1;
  xlv_COLOR0 = mix (vColor1, vColor, vec4(tmpvar_10));
  xlv_TEXCOORD0 = tmpvar_2;
  xlv_TEXCOORD1 = tmpvar_11;
}

