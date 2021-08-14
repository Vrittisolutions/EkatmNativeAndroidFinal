uniform highp mat4 matProj;
uniform highp mat4 matWorld;
uniform highp mat4 matWorldView;
uniform highp vec4 PackedFloatParams;
uniform highp vec4 WaterFallSliceOffsset;
uniform highp vec4 TextureDimensionsInv;
uniform highp vec4 WorldXZOffsetSize;
uniform sampler2D HeightMapTexture;
attribute highp vec4 vPosition;
attribute highp vec3 vNormal;
attribute highp vec4 vColor;
attribute highp vec4 vTexCoord1;
attribute highp vec4 vTexCoord2;
attribute highp vec4 vColor1;
attribute highp vec4 vColor2;
varying highp vec4 xlv_COLOR0;
varying highp vec4 xlv_TEXCOORD1;
varying highp vec3 xlv_TEXCOORD2;
varying highp vec4 xlv_COLOR1;
varying highp vec4 xlv_COLOR2;
void main ()
{
  highp vec4 tmpvar_1;
  highp vec4 tmpvar_2;
  tmpvar_1.w = vPosition.w;
  tmpvar_2.xw = vTexCoord1.xw;
  highp vec2 tmpvar_3;
  tmpvar_3.x = vTexCoord1.x;
  tmpvar_3.y = WaterFallSliceOffsset.y;
  highp vec2 _texCoord_4;
  _texCoord_4 = ((tmpvar_3 - WorldXZOffsetSize.xy) * (1.0/(WorldXZOffsetSize.zw)));
  _texCoord_4.x = mix (TextureDimensionsInv.x, TextureDimensionsInv.z, _texCoord_4.x);
  _texCoord_4.y = mix (TextureDimensionsInv.y, TextureDimensionsInv.w, _texCoord_4.y);
  lowp vec4 tmpvar_5;
  tmpvar_5 = texture2DLod (HeightMapTexture, _texCoord_4, 0.0);
  highp vec4 rgba_6;
  rgba_6 = tmpvar_5.zyxw;
  tmpvar_2.y = clamp ((vTexCoord1.y * (
    (dot (rgba_6.wzyx, vec4(1.0, 0.003921569, 1.53787e-5, 6.030863e-8)) * PackedFloatParams.y)
   + PackedFloatParams.x)), WaterFallSliceOffsset.z, WaterFallSliceOffsset.w);
  tmpvar_1.xyz = (vPosition.xyz * vTexCoord2.xyz);
  tmpvar_2.z = (vTexCoord1.z + WaterFallSliceOffsset.y);
  tmpvar_1.xyz = (tmpvar_1.xyz + tmpvar_2.xyz);
  tmpvar_1.w = 1.0;
  highp mat3 tmpvar_7;
  tmpvar_7[0] = matWorld[0].xyz;
  tmpvar_7[1] = matWorld[1].xyz;
  tmpvar_7[2] = matWorld[2].xyz;
  gl_Position = (tmpvar_1 * (matWorldView * matProj));
  xlv_COLOR0 = vColor;
  xlv_TEXCOORD1 = (tmpvar_1 * matWorld);
  xlv_TEXCOORD2 = (vNormal * tmpvar_7);
  xlv_COLOR1 = vColor1;
  xlv_COLOR2 = vColor2;
}

